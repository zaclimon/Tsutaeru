package com.zaclimon.acetv.service;

import android.app.job.JobParameters;
import android.content.Context;
import android.content.SharedPreferences;

import com.zaclimon.acetv.R;
import com.zaclimon.acetv.properties.AceChannelProperties;
import com.zaclimon.acetv.util.Constants;
import com.zaclimon.xipl.properties.ChannelProperties;
import com.zaclimon.xipl.service.ProviderEpgService;


/**
 * Custom {@link ProviderEpgService} adapted for syncing Ace TV content.
 * <p>
 * It downloads and parses the content from a user's M3U playlist + EPG XMLTV to add it to the
 * Android TV's system database in order to be used by the Live Channels application.
 *
 * @author zaclimon
 * Creation date: 11/06/17
 */

public class AceJobService extends ProviderEpgService {

    private ChannelProperties mChannelProperties;

    @Override
    public boolean onStartJob(JobParameters params) {
        mChannelProperties = new AceChannelProperties(getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE));
        return (super.onStartJob(params));
    }

    @Override
    public String getPlaylistUrl() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");
        String streamType = sharedPreferences.getString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS);
        return (getString(R.string.ace_playlist_url, username, password, streamType));
    }

    @Override
    public String getEpgUrl() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");
        return (getString(R.string.ace_epg_url, username, password));
    }

    @Override
    public ChannelProperties getChannelProperties() {
        return (mChannelProperties);
    }
}
