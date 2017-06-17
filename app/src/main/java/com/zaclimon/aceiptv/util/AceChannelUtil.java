package com.zaclimon.aceiptv.util;

import android.media.tv.TvContract;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.InternalProviderData;
import com.google.android.media.tv.companionlibrary.utils.TvContractUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by isaac on 17-06-09.
 */

public class AceChannelUtil {

    private static final String ATTRIBUTE_TVG_ID = "tvg-id";
    private static final String ATTRIBUTE_LINK = "link";
    private static final String ATTRIBUTE_TVG_LOGO = "tvg-logo";
    private static final String ATTRIBUTE_TVG_NAME = "tvg-name";
    private static final String ATTRIBUTE_GROUP_TITLE = "group-title";

    public static String ACE_IPTV_PREFERENCES = "AceSharedPreferences";
    public static String USERNAME_PREFERENCE = "username";
    public static String PASSWORD_PREFERENCE = "password";

    public static final String ORIGINAL_NETWORK_ID_PROVIDER = "original_network_id";

    private static final String LOG_TAG = "AceChannelUtil";

    public static List<Channel> getChannelList(InputStream playlist, List<Channel> channels) {

        List<String> playListString = getPlaylistAsStrings(playlist);

        List<String> names = getChannelAttribute(playListString, ATTRIBUTE_TVG_NAME);
        List<String> links = getChannelAttribute(playListString, ATTRIBUTE_LINK);
        List<String> logos = getChannelAttribute(playListString, ATTRIBUTE_TVG_LOGO);
        List<String> ids = getChannelAttribute(playListString, ATTRIBUTE_TVG_ID);
        List<Channel> tempList = new ArrayList<>();

        /*
         Google is kind of "weird" when it comes to the way it has of tuning channel/program
         data. In most cases, the TIF expects to have a program available for each channels in order
         to play them.

         However, ACE doesn't have all programs available for a given channel so let's
         retrieve all channels with programs in the EPG first. Then, we'll retrieve all channels that
         doesn't have an entry in the EPG.

         This is definitely not the most efficient way of retrieving channels since we're doing at most
         the following operation:

         ((Number of entries in the playlist) * (the number of channels available in the EPG))

         Which can get high pretty quickly. However, it is the only way I've found it to be made
         in one set of loops.
         */

        for (int i = 0; i < names.size(); i++) {
            String tempName = names.get(i);
            String tempLogo = logos.get(i);
            String tempLink = links.get(i);
            Channel channel;
            int tempId = ids.get(i).hashCode();

            for (int j = 0; j < channels.size(); j++) {

                /*
                 If the id from the channel is the same as the one found from the XMLTvParser or
                 if we're at the end of the EPG listed channels, create a custom channel to return
                 to for the EpgJobService.
                 */

                Channel tempChannel = channels.get(j);

                if (tempId == tempChannel.getOriginalNetworkId() || j == channels.size() - 1) {
                    channel = createChannel(tempName, Integer.toString(i + 1), tempId, tempLogo, tempLink);
                    tempList.add(channel);
                    break;
                }
            }
        }
        return (tempList);
    }

    private static List<String> getPlaylistAsStrings(InputStream playlist) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(playlist));
        List<String> tempList = new ArrayList<>();
        String line;

        try {
            while ((line = bufferedReader.readLine()) != null) {
                tempList.add(line);
            }

            return (tempList);
        } catch (IOException io) {
            // Couldn't read the stream
        }
        return (null);
    }

    private static List<String> getChannelAttribute(List<String> playlist, String attribute) {

        List<String> attributes = new ArrayList<>();

        for (String line : playlist) {
            // Be sure we're on the channel's information line and not the link one first
            if (line.startsWith("#EXTINF") && !attribute.equals(ATTRIBUTE_LINK)) {

                int indexAttributeStart;
                int indexAttributeEnd;

                switch (attribute) {
                    case ATTRIBUTE_TVG_ID:
                        indexAttributeStart = line.indexOf(ATTRIBUTE_TVG_ID);
                        indexAttributeEnd = line.indexOf(ATTRIBUTE_TVG_NAME);
                        break;
                    case ATTRIBUTE_TVG_NAME:
                        indexAttributeStart = line.indexOf(ATTRIBUTE_TVG_NAME);
                        indexAttributeEnd = line.indexOf(ATTRIBUTE_TVG_LOGO);
                        break;
                    case ATTRIBUTE_TVG_LOGO:
                        indexAttributeStart = line.indexOf(ATTRIBUTE_TVG_LOGO);
                        indexAttributeEnd = line.indexOf(ATTRIBUTE_GROUP_TITLE);
                        break;
                    case ATTRIBUTE_GROUP_TITLE:
                        indexAttributeStart = line.indexOf(ATTRIBUTE_GROUP_TITLE);
                        indexAttributeEnd = line.length() - 1;
                        break;
                    default:
                        throw new IllegalArgumentException("Wrong attribute: " + attribute);
                }

                String attributePart = line.substring(indexAttributeStart, indexAttributeEnd);

                String[] realPart = attributePart.split("=");

                /*
                Trim just to be sure in order to not have unpleasant surprises...
                (Strings with different hashcodes because of a space for example.)
                */

                attributes.add(realPart[1].replace("\"", "").trim());

            } else if (line.startsWith("http://") && attribute.equals(ATTRIBUTE_LINK)) {
                attributes.add(line);
            }
        }

        return (attributes);
    }

    private static String getVideoFormat(String displayName) {

        if (displayName.contains("FHD")) {
            return (TvContract.Channels.VIDEO_FORMAT_1080P);
        } else if (displayName.contains("HD") || displayName.contains("ES")) {
            return (TvContract.Channels.VIDEO_FORMAT_720P);
        } else if (displayName.contains("UK") || displayName.contains("FR") || displayName.contains("DE")) {
            return (TvContract.Channels.VIDEO_FORMAT_576P);
        } else {
            return (TvContract.Channels.VIDEO_FORMAT_480P);
        }
    }

    private static Channel createChannel(String displayName, String displayNumber, int originalNetworkId, String logo, String url) {

        Channel.Builder builder = new Channel.Builder();
        InternalProviderData internalProviderData = new InternalProviderData();

        /*
         Set the original network id so we can retrieve it from the TV listing because
         it somewhat gets changed after adding it to a EpgSyncTask.
        */
        try {
            internalProviderData.put(ORIGINAL_NETWORK_ID_PROVIDER, originalNetworkId);
        } catch (InternalProviderData.ParseException ps) {
            // Can't do anything about this...
        }

        internalProviderData.setVideoUrl(url);
        builder.setDisplayName(displayName);
        builder.setDisplayNumber(displayNumber);
        builder.setOriginalNetworkId(originalNetworkId);
        builder.setChannelLogo(logo);
        builder.setVideoFormat(getVideoFormat(displayName));
        builder.setInternalProviderData(internalProviderData);
        return (builder.build());
    }
}
