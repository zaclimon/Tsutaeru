package com.zaclimon.aceiptv.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.InternalProviderData;
import com.zaclimon.aceiptv.service.AceInputTvService;

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

    public static final ComponentName TV_INPUT_SERVICE_COMPONENT = new ComponentName(AceInputTvService.class.getPackage().getName(), AceInputTvService.class.getName());

    private static final String LOG_TAG = "AceChannelUtil";

    public static List<Channel> getChannelList(InputStream playlist, List<Channel> channels, Context context) {

        List<String> playListString = getPlaylistAsStrings(playlist);
        List<String> names = getChannelAttribute(playListString, Constants.ATTRIBUTE_TVG_NAME);
        List<String> links = getChannelAttribute(playListString, Constants.ATTRIBUTE_LINK);
        List<String> logos = getChannelAttribute(playListString, Constants.ATTRIBUTE_TVG_LOGO);
        List<String> ids = getChannelAttribute(playListString, Constants.ATTRIBUTE_TVG_ID);
        List<Channel> tempList = new ArrayList<>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.ACE_IPTV_PREFERENCES, Context.MODE_PRIVATE);

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
            boolean hasChannelLogo = sharedPreferences.getBoolean(Constants.CHANNEL_LOGO_PREFERENCE, true);

            for (int j = 0; j < channels.size(); j++) {

                /*
                 If the id from the channel is the same as the one found from the XMLTvParser or
                 if we're at the end of the EPG listed channels, create a custom channel to return
                 to for the EpgJobService.
                 */

                Channel tempChannel = channels.get(j);

                if (tempId == tempChannel.getOriginalNetworkId() || j == channels.size() - 1) {
                    if (hasChannelLogo) {
                        channel = createChannel(tempName, Integer.toString(i + 1), tempId, tempLogo, tempLink);
                    } else {
                        channel = createChannel(tempName, Integer.toString(i + 1), tempId, null, tempLink);
                    }
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

            playlist.close();
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
            if (line.startsWith("#EXTINF") && !attribute.equals(Constants.ATTRIBUTE_LINK)) {

                int indexAttributeStart;
                int indexAttributeEnd;

                switch (attribute) {
                    case Constants.ATTRIBUTE_TVG_ID:
                        indexAttributeStart = line.indexOf(Constants.ATTRIBUTE_TVG_ID);
                        indexAttributeEnd = line.indexOf(Constants.ATTRIBUTE_TVG_NAME);
                        break;
                    case Constants.ATTRIBUTE_TVG_NAME:
                        indexAttributeStart = line.indexOf(Constants.ATTRIBUTE_TVG_NAME);
                        indexAttributeEnd = line.indexOf(Constants.ATTRIBUTE_TVG_LOGO);
                        break;
                    case Constants.ATTRIBUTE_TVG_LOGO:
                        indexAttributeStart = line.indexOf(Constants.ATTRIBUTE_TVG_LOGO);
                        indexAttributeEnd = line.indexOf(Constants.ATTRIBUTE_GROUP_TITLE);
                        break;
                    case Constants.ATTRIBUTE_GROUP_TITLE:
                        indexAttributeStart = line.indexOf(Constants.ATTRIBUTE_GROUP_TITLE);
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

            } else if (line.startsWith("http://") && attribute.equals(Constants.ATTRIBUTE_LINK)) {
                attributes.add(line);
            }
        }

        return (attributes);
    }

    private static Channel createChannel(String displayName, String displayNumber, int epgId, String logo, String url) {

        /*
         In order to map correctly the programs to a given channel, store the EPG id somewhere in the
         channel so we can retrieve it when we'll need to find programs

         Using the EPG ID as a good way to have an original network id but it might create channel
         duplicates. Since some channels either don't have an EPG id (which makes 0 as a hash) or might
         share the same id altogether, (same channel in SD/HD for example) they get recreated
         as their original id isn't really original anymore...

         In that case, let's use the display name as the original network id instead of the EPG id.
        */

        Channel.Builder builder = new Channel.Builder();
        InternalProviderData internalProviderData = new InternalProviderData();

        try {
            internalProviderData.put(Constants.EPG_ID_PROVIDER, epgId);
        } catch (InternalProviderData.ParseException ps) {
            // Can't do anything about this...
        }

        internalProviderData.setVideoUrl(url);
        builder.setDisplayName(displayName);
        builder.setDisplayNumber(displayNumber);
        builder.setOriginalNetworkId(displayName.hashCode());
        builder.setChannelLogo(logo);
        builder.setInternalProviderData(internalProviderData);
        return (builder.build());
    }
}
