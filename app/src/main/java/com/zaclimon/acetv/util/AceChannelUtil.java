package com.zaclimon.acetv.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.media.tv.companionlibrary.XmlTvParser;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.InternalProviderData;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.data.AvContent;
import com.zaclimon.acetv.service.AceTvInputService;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Utility class used for retrieving, handling Ace TV stuff.
 *
 * @author zaclimon
 * Creation date: 09/06/17
 */

public class AceChannelUtil {

    /**
     * {@link ComponentName} used for defining easily an input id for the application
     */
    public static final ComponentName TV_INPUT_SERVICE_COMPONENT = new ComponentName("com.zaclimon.acetv", AceTvInputService.class.getName());

    private static final String LOG_TAG = "AceChannelUtil";

    /**
     * Gets a list of channels based on the M3U playlist of a given user.
     * @param playlist List of the playlist lines containing the user's channels
     * @param context the context required for some other operations (Adding the logo for example)
     * @return the list of channels for a given user
     */
    public static List<Channel> createChannelList(InputStream playlist, Context context) {

        List<AvContent> channelContents = AvContentUtil.getAvContentsList(playlist);
        List<Channel> tempList = new ArrayList<>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);
        boolean hasChannelLogo = sharedPreferences.getBoolean(Constants.CHANNEL_LOGO_PREFERENCE, true);

        /*
         Google is kind of "weird" when it comes to the way it has of tuning channel/program
         data. In most cases, the TIF expects to have a program available for each channels in order
         to play them.

         However, ACE doesn't have all programs available for a given channel so simply save the EPG
         id and leave the parsing to when programs will get created.
         */

        for (int i = 0; i < channelContents.size(); i++) {
            Channel channel;
            String tempName = channelContents.get(i).getTitle();
            String tempLogo = channelContents.get(i).getLogo();
            String tempLink = channelContents.get(i).getContentLink();
            int tempId = channelContents.get(i).getId();

            if (hasChannelLogo) {
                channel = createChannel(tempName, Integer.toString(i + 1), tempId, tempLogo, tempLink, getProgramGenre(tempName, context));
            } else {
                channel = createChannel(tempName, Integer.toString(i + 1), tempId, null, tempLink, getProgramGenre(tempName, context));
            }

            // Premium users might have VOD content in their playlist, don't include them.
            if (isLiveChannel(channel)) {
                tempList.add(channel);
            }
        }
        return (tempList);
    }

    /**
     * Creates a {@link Channel} that can be used by the Android TV framework and the Live Channels application.
     * @param displayName the display name of the channel
     * @param displayNumber the display number of the channel
     * @param epgId the id as defined in {@link com.google.android.media.tv.companionlibrary.XmlTvParser}
     * @param logo the logo url link
     * @param url the video url link
     * @return the channel to be used by the system.
     */
    private static Channel createChannel(String displayName, String displayNumber, int epgId, String logo, String url, String[] genres) {

        /*
         In order to map correctly the programs to a given channel, store the EPG id somewhere in the
         channel so we can retrieve it when we'll need to find programs

         Using the EPG ID as a good way to have an original network id but it might create channel
         duplicates. Since some channels either don't have an EPG id (which makes 0 as a hash) or might
         share the same id altogether, (same channel in SD/HD for example) they get recreated
         as their original id isn't really original anymore...

         In that case, let's use the display name as the original network id instead of the EPG id.

         Let's also retrieve the an example genre for the channel so it can be passed on the side
         of the EPG guide.
        */

        Channel.Builder builder = new Channel.Builder();
        InternalProviderData internalProviderData = new InternalProviderData();

        try {
            JSONArray genresJsonArray = new JSONArray(genres);
            internalProviderData.put(Constants.EPG_ID_PROVIDER, epgId);
            internalProviderData.put(Constants.CHANNEL_GENRES_PROVIDER, genresJsonArray);
        } catch (InternalProviderData.ParseException ps) {
            // Can't do anything about this...
        } catch (JSONException json) {
            json.printStackTrace();
        }

        internalProviderData.setVideoUrl(url);
        builder.setDisplayName(displayName);
        builder.setDisplayNumber(displayNumber);
        builder.setOriginalNetworkId(displayName.hashCode());
        builder.setChannelLogo(logo);
        builder.setInternalProviderData(internalProviderData);
        return (builder.build());
    }

    /**
     * Gives one or more genre(s) for a given {@link com.google.android.media.tv.companionlibrary.model.Program}
     * based on it's channel. The genre(s) must be one of the following:
     *
     * {@link android.media.tv.TvContract.Programs.Genres#FAMILY_KIDS}
     * {@link android.media.tv.TvContract.Programs.Genres#SPORTS}
     * {@link android.media.tv.TvContract.Programs.Genres#SHOPPING}
     * {@link android.media.tv.TvContract.Programs.Genres#MOVIES}
     * {@link android.media.tv.TvContract.Programs.Genres#COMEDY}
     * {@link android.media.tv.TvContract.Programs.Genres#TRAVEL}
     * {@link android.media.tv.TvContract.Programs.Genres#DRAMA}
     * {@link android.media.tv.TvContract.Programs.Genres#EDUCATION}
     * {@link android.media.tv.TvContract.Programs.Genres#ANIMAL_WILDLIFE}
     * {@link android.media.tv.TvContract.Programs.Genres#NEWS}
     * {@link android.media.tv.TvContract.Programs.Genres#GAMING}
     * {@link android.media.tv.TvContract.Programs.Genres#ARTS}
     * {@link android.media.tv.TvContract.Programs.Genres#ENTERTAINMENT}
     * {@link android.media.tv.TvContract.Programs.Genres#LIFE_STYLE}
     * {@link android.media.tv.TvContract.Programs.Genres#MUSIC}
     * {@link android.media.tv.TvContract.Programs.Genres#PREMIER}
     * {@link android.media.tv.TvContract.Programs.Genres#TECH_SCIENCE}
     *
     * @param channelName The name of the channel
     * @param context context required to access a string containing the channel names
     * @return the genre of the channel as specified by in {@link android.media.tv.TvContract.Programs.Genres}
     */
    private static String[] getProgramGenre(String channelName, Context context) {

         /*
         Another thing that is weird with Google is that a given channel has a genre that shows
         content based on it. However, it is the programs that has them.

         I understand where the decision could come from since a Program could have a genre different
         from it's channel. Nonetheless, it's the only way to do some kind of easy grouping in the
         Live Channels app.
         */

        List<String[]> allChannelGroups = new ArrayList<>();
        List<String> currentChannelGroups = new ArrayList<>();

        // Add all the groups now so we know we won't have any problems when comparing.
        allChannelGroups.add(context.getResources().getStringArray(R.array.animal_wildlife_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.arts_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.comedy_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.drama_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.education_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.entertainment_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.family_kids_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.gaming_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.lifestyle_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.movies_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.music_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.news_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.premier_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.shopping_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.sports_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.tech_science_channels));
        allChannelGroups.add(context.getResources().getStringArray(R.array.travel_channels));

        for (int i = 0; i < allChannelGroups.size(); i++) {
            String[] currentGroup = allChannelGroups.get(i);
            for (String channelType : currentGroup) {
                if (channelName.contains(channelType)) {
                    currentChannelGroups.add(getGenreByPosition(i));
                    break;
                }
            }
        }

        String[] channelGroupsArray = new String[currentChannelGroups.size()];
        channelGroupsArray = currentChannelGroups.toArray(channelGroupsArray);
        return (channelGroupsArray);
    }

    /**
     * Calculates the current date with the last half hour passed.
     * @return the current date with the last half hour in milliseconds
     */
    public static long getLastHalfHourMillis() {
        return (getLastHalfHourMillis(System.currentTimeMillis()));
    }

    /**
     * Calculates the current date with the last half hour passed
     * @param originalMillis the desired calculated time in milliseconds
     * @return the desired date with the last half hour in milliseconds
     */
    public static long getLastHalfHourMillis(long originalMillis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(originalMillis);

        int minutes = calendar.get(Calendar.MINUTE);
        int difference = minutes % 30;

        if (difference != 0) {
            minutes -= difference;
        }

        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (calendar.getTimeInMillis());
    }

    /**
     * Gives a Program's genre based on the {@link Constants#CHANNEL_GENRES} array position. If
     * comparing must be made, the order should be based on this particular array as well.
     *
     * @param position the position of the wished genre.
     * @return the genre literal as defined in {@link android.media.tv.TvContract.Programs.Genres}.
     */
    private static String getGenreByPosition(int position) {
        return (Constants.CHANNEL_GENRES[position]);
    }

    /**
     * Gives all possible genres for a given channel based on it's json contents.
     * @param json the json parsed as a String.
     * @return the array of all the genres for the channel.
     */
    public static String[] getGenresArrayFromJson(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            String[] genresArray = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                genresArray[i] = jsonArray.getString(i);
            }
            return (genresArray);
        } catch (JSONException js) {
            js.printStackTrace();
        }
        return (null);
    }

    /**
     * Verifies if a given channel is a live one
     * @param channel the channel to be verified
     * @return true if the channel streams live content.
     */
    private static boolean isLiveChannel(Channel channel) {
        InternalProviderData internalProviderData = channel.getInternalProviderData();
        return (internalProviderData != null && internalProviderData.getVideoUrl().contains("/live/"));
    }

}
