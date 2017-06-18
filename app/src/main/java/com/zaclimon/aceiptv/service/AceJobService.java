package com.zaclimon.aceiptv.service;

import android.app.job.JobParameters;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.google.android.media.tv.companionlibrary.XmlTvParser;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.InternalProviderData;
import com.google.android.media.tv.companionlibrary.model.Program;
import com.google.android.media.tv.companionlibrary.utils.TvContractUtils;
import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.util.AceChannelUtil;
import com.zaclimon.aceiptv.util.Constants;
import com.zaclimon.aceiptv.util.RichFeedUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by isaac on 17-06-11.
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
         A weird thing happening is that upon registering the channel list into the EpgSyncJobService,
         The channels have different original network ID's from the one defined when getChannelList()
         is called.

         In that case, since we have the original value stored inside the InternalProviderData of the
         channel, retrieve it and compare it against the channel id's in a given Program.
         */

        List<Program> listingPrograms = mTvListing.getAllPrograms();
        List<Program> tempPrograms;
        InternalProviderData internalProviderData = channel.getInternalProviderData();

        try {
            if (internalProviderData != null && internalProviderData.has(Constants.ORIGINAL_NETWORK_ID_PROVIDER)) {
                // The provider data gets parsed as a string by default
                String originalNetworkIdString = (String) internalProviderData.get(Constants.ORIGINAL_NETWORK_ID_PROVIDER);
                int originalNetworkIdInt = Integer.parseInt(originalNetworkIdString);
                tempPrograms = new ArrayList<>();

                for (Program program : listingPrograms) {
                    if (program.getChannelId() == originalNetworkIdInt) {
                        tempPrograms.add(program);
                    }
                }
                return (tempPrograms);
            }
        } catch (InternalProviderData.ParseException ps) {
            Log.e(LOG_TAG, "Channel " + channel.getDisplayName() + " Couldn't get checked");
            return (null);
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

        public void deleteChannelsFromContentProvider() {

            /*
              Because of the weird issue occurring to the original id's, most of the channels get
              updated whilst some other ones get added again.

              For this reason, delete all the channels coming from Ace in the ContentProvider
              although it is not recommended as per the Android SDK.
             */

            ContentResolver contentResolver = getContentResolver();
            List<Channel> channels = TvContractUtils.getChannels(contentResolver);

            if (mInputId != null && channels != null && !channels.isEmpty()) {
                getContentResolver().delete(TvContract.buildChannelsUriForInput(mInputId), null, null);
            }
        }

        @Override
        public Boolean doInBackground(Void... params) {
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.ACE_IPTV_PREFERENCES, MODE_PRIVATE);
            String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
            String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");

            String playListUrl = getString(R.string.ace_playlist_url, username, password);
            String epgUrl = getString(R.string.ace_epg_url, username, password);

            try {
                InputStream inputStream = RichFeedUtil.getInputStream(AceJobService.this, Uri.parse(playListUrl));
                mTvListing = RichFeedUtil.getRichTvListings(AceJobService.this, epgUrl);
                mChannels = AceChannelUtil.getChannelList(inputStream, mTvListing.getChannels());
                deleteChannelsFromContentProvider();
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
