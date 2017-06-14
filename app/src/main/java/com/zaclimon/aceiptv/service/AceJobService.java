package com.zaclimon.aceiptv.service;

import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.api.BooleanResult;
import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.google.android.media.tv.companionlibrary.XmlTvParser;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.Program;
import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.util.AceChannelUtil;
import com.zaclimon.aceiptv.util.RichFeedUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by isaac on 17-06-11.
 */

public class AceJobService extends EpgSyncJobService {

    private final String LOG_TAG = getClass().getSimpleName();

    private List<Channel> mChannels;
    private XmlTvParser.TvListing mTvListing;

    @Override
    public List<Channel> getChannels() {
        return (mChannels);
    }

    @Override
    public List<Program> getProgramsForChannel(Uri channelUri, Channel channel, long startMs, long endMs) {
        return (mTvListing != null ? mTvListing.getPrograms(channel) : null);
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        // Broadcast status
        Intent intent = new Intent(ACTION_SYNC_STATUS_CHANGED);
        intent.putExtra(BUNDLE_KEY_INPUT_ID, params.getExtras().getString(BUNDLE_KEY_INPUT_ID));
        Log.d(LOG_TAG, "Sync program data for " + params.getExtras().getString(BUNDLE_KEY_INPUT_ID));
        intent.putExtra(SYNC_STATUS, SYNC_STARTED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        AsyncEpgDownload asyncEpgDownload = new AsyncEpgDownload(params);
        asyncEpgDownload.execute();
        return (true);
    }


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
            SharedPreferences sharedPreferences = getSharedPreferences(AceChannelUtil.ACE_IPTV_PREFERENCES, MODE_PRIVATE);
            String username = sharedPreferences.getString(AceChannelUtil.USERNAME_PREFERENCE, "");
            String password = sharedPreferences.getString(AceChannelUtil.PASSWORD_PREFERENCE, "");
            String playListUrl = getString(R.string.ace_playlist_url, username, password);
            String epgUrl = getString(R.string.ace_epg_url, username, password);

            try {
                InputStream inputStream = RichFeedUtil.getInputStream(AceJobService.this, Uri.parse(playListUrl));
                mTvListing = RichFeedUtil.getRichTvListings(AceJobService.this, epgUrl);
                mChannels = AceChannelUtil.getChannelList(inputStream, mTvListing.getChannels());
                Log.d(LOG_TAG, "Program size: " + mTvListing.getAllPrograms().size());
                return (true);
            } catch (IOException io) {
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
