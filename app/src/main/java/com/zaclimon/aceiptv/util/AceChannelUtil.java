package com.zaclimon.aceiptv.util;

import android.media.tv.TvContract;
import android.util.Log;

import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.InternalProviderData;

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

            for (int j = 0; j < channels.size(); j++) {

                Channel tempChannel = channels.get(j);

                if (tempChannel.getDisplayName().contains(tempName)) {
                    InternalProviderData internalProviderData = tempChannel.getInternalProviderData();

                    if (internalProviderData == null) {
                        internalProviderData = new InternalProviderData();
                    }

                    Channel.Builder builder = new Channel.Builder(tempChannel);

                    internalProviderData.setVideoUrl(links.get(j));
                    builder.setVideoFormat(getVideoFormat(tempChannel.getDisplayName()));
                    builder.setInternalProviderData(internalProviderData);
                    tempList.add(builder.build());
                    break;
                }

                /*
                 If we're at the end of the EPG listed channels, create a custom channel so we can
                 still fit it into our current list since it is in the playlist.
                 */
                if (j == channels.size() - 1) {
                    InternalProviderData internalProviderData = new InternalProviderData();
                    Channel.Builder builder = new Channel.Builder();

                    builder.setDisplayName(tempName);
                    builder.setOriginalNetworkId(ids.get(i).hashCode());
                    builder.setChannelLogo(logos.get(i));
                    builder.setVideoFormat(getVideoFormat(tempName));
                    internalProviderData.setVideoUrl(links.get(i));
                    builder.setInternalProviderData(internalProviderData);
                    tempList.add(builder.build());
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
                attributes.add(realPart[1].replace("\"", ""));

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
}
