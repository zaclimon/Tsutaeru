package com.zaclimon.acetv.service;

import android.app.job.JobParameters;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.google.android.media.tv.companionlibrary.XmlTvParser;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.InternalProviderData;
import com.google.android.media.tv.companionlibrary.model.Program;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.util.AceChannelUtil;
import com.zaclimon.acetv.util.Constants;
import com.zaclimon.acetv.util.RichFeedUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Custom {@link EpgSyncJobService} used for syncing Ace TV content.
 * <p>
 * It downloads and parses the content from a user's M3U playlist + EPG XMLTV to add it to the
 * Android TV's system database in order to be used by the Live Channels application.
 *
 * @author zaclimon
 * Creation date: 11/06/17
 */

public class AceJobService extends EpgSyncJobService {

    private final String LOG_TAG = getClass().getSimpleName();

    private List<Channel> mChannels;
    private String mInputId;
    private XmlTvParser.TvListing mTvListing;

    @Override
    public List<Channel> getChannels() {
        return (mChannels);
    }

    @Override
    public List<Program> getProgramsForChannel(Uri channelUri, Channel channel, long startMs, long endMs) {

        /*
         The XMLTV file from the EPG contains an ID that might be used across one or several channels.
         In that case, it will be stored in the internal provider data of the given channel.

         XmlTvParser has a internal HashMap in which Programs can be retrieved. Let's use it as our
         primary means of getting them. If there aren't any available programs for a channel,
         create a dummy program so the EPG guide from "Live Channels" can categorize the channel.

         Finally, retrieve a given channel's genre based on it's internal provider data. This
         information will be passed to the Live Channels's guide.
         */

        InternalProviderData internalProviderData = channel.getInternalProviderData();

        try {
            if (internalProviderData != null && internalProviderData.has(Constants.EPG_ID_PROVIDER)) {

                // The provider data gets parsed as a string by default, same for the genres.
                List<Program> tempPrograms = new ArrayList<>();
                String epgId = (String) internalProviderData.get(Constants.EPG_ID_PROVIDER);
                String channelGenresJson = (String) internalProviderData.get(Constants.CHANNEL_GENRES_PROVIDER);
                int epgIdInt = Integer.parseInt(epgId);

                if (epgIdInt != 0) {
                    tempPrograms = mTvListing.getPrograms(epgIdInt);
                }

                // Create a dummy program for listing a channel's genre if there are no programs.
                if (tempPrograms.isEmpty()) {
                    Program.Builder builder = new Program.Builder(channel);
                    long startTimeMillis = AceChannelUtil.getLastHalfHourMillis();
                    long endTimeMillis = startTimeMillis + TimeUnit.DAYS.toMillis(7);
                    builder.setStartTimeUtcMillis(startTimeMillis);
                    builder.setEndTimeUtcMillis(endTimeMillis);
                    builder.setBroadcastGenres(AceChannelUtil.getGenresArrayFromJson(channelGenresJson));
                    tempPrograms.add(builder.build());
                } else {
                    // Set genres a given channel if it has programs.
                    for (int i = 0; i < tempPrograms.size(); i++) {
                        Program.Builder builder = new Program.Builder(tempPrograms.get(i));
                        builder.setBroadcastGenres(AceChannelUtil.getGenresArrayFromJson(channelGenresJson));
                        tempPrograms.set(i, builder.build());
                    }
                }

                return (tempPrograms);
            }
        } catch (InternalProviderData.ParseException ps) {
            Log.e(LOG_TAG, "Channel " + channel.getDisplayName() + " Couldn't get checked");
        }

        return (null);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // Broadcast status
        mInputId = params.getExtras().getString(BUNDLE_KEY_INPUT_ID);
        Intent intent = new Intent(ACTION_SYNC_STATUS_CHANGED);
        intent.putExtra(BUNDLE_KEY_INPUT_ID, mInputId);
        Log.d(LOG_TAG, "Sync program data for " + mInputId);
        intent.putExtra(SYNC_STATUS, SYNC_STARTED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        AsyncEpgDownload asyncEpgDownload = new AsyncEpgDownload(params);
        asyncEpgDownload.execute();
        return (true);
    }


    /**
     * Custom class used to download in a asynchronous fashion the M3U playlist as well
     * as the EPG guide from a user.
     *
     * @author zaclimon
     *         Creation date: 11/06/17
     */
    public class AsyncEpgDownload extends AsyncTask<Void, Void, Boolean> {

        /*
         Retrieve username and password from SharedPreferences. We shouldn't worry about not getting
         them since the user will get to AuthActivity during the initial configuration.

         Worst case, if a user decides to wipe the app data whilst the channel are set up, he/she
         can still enter to the app to get it's info re-saved again.
         */

        private JobParameters mJobParameters;

        public AsyncEpgDownload(JobParameters jobParameters) {
            mJobParameters = jobParameters;
        }

        @Override
        public Boolean doInBackground(Void... params) {
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.ACE_TV_PREFERENCES, MODE_PRIVATE);
            String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
            String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");
            String streamType = sharedPreferences.getString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS);
            String playListUrl = getString(R.string.ace_playlist_url, username, password, streamType);
            String epgUrl = getString(R.string.ace_epg_url, username, password);

            try {
                InputStream inputStream = RichFeedUtil.getInputStream(AceJobService.this, Uri.parse(playListUrl));

                mTvListing = RichFeedUtil.getRichTvListings(AceJobService.this, epgUrl);
                mChannels = AceChannelUtil.createChannelList(inputStream, AceJobService.this);

                return (mTvListing != null && mChannels != null);
            } catch (IOException io) {
                io.printStackTrace();
                return (false);
            }
        }

        @Override
        public void onPostExecute(Boolean result) {

            if (result) {
                EpgSyncTask epgSyncTask = new EpgSyncTask(mJobParameters);
                epgSyncTask.execute();
            } else {
                Log.w(LOG_TAG, "Couldn't retrieve the playlist/EPG");
            }
        }
    }
}
