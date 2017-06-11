package com.zaclimon.aceiptv.service;

import android.content.SharedPreferences;
import android.net.Uri;

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

    @Override
    public List<Channel> getChannels() {

        /*
         Retrieve username and password from SharedPreferences. We shouldn't worry about not getting
         them since the user will get to AuthActivity during the initial configuration.

         Worst case, if a user decides to wipe the app data whilst the channel are set up, he/she
         can still enter to the app to get it's info re-saved again.
         */

        SharedPreferences sharedPreferences = getSharedPreferences(AceChannelUtil.ACE_IPTV_PREFERENCES, MODE_PRIVATE);
        String username = sharedPreferences.getString(AceChannelUtil.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(AceChannelUtil.PASSWORD_PREFERENCE, "");
        String playListUrl = getString(R.string.ace_playlist_url, username, password);
        String epgUrl = getString(R.string.ace_epg_url, username, password);

        try  {
            InputStream inputStream = RichFeedUtil.getInputStream(this, Uri.parse(playListUrl));
            XmlTvParser.TvListing tempListing = RichFeedUtil.getRichTvListings(this, epgUrl);
            return (AceChannelUtil.getChannelList(inputStream, tempListing.getChannels()));
        } catch (IOException io) {
            io.printStackTrace();
            return (null);
        }
    }

    @Override
    public List<Program> getProgramsForChannel(Uri channelUri, Channel channel, long startMs, long endMs) {
        SharedPreferences sharedPreferences = getSharedPreferences(AceChannelUtil.ACE_IPTV_PREFERENCES, MODE_PRIVATE);
        String username = sharedPreferences.getString(AceChannelUtil.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(AceChannelUtil.PASSWORD_PREFERENCE, "");
        String epgUrl = getString(R.string.ace_epg_url, username, password);

        XmlTvParser.TvListing listing = RichFeedUtil.getRichTvListings(this, epgUrl);
        return (listing.getPrograms(channel));
    }
}
