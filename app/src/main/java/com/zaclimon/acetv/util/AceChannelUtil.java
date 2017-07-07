package com.zaclimon.acetv.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.media.tv.companionlibrary.XmlTvParser;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.InternalProviderData;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.service.AceTvInputService;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
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
     * Creates or updates a channel list based on a M3U playlist and a given reference channel list
     * @param playlist the stream containing the M3U playlist
     * @param channels the reference channel list used (Might be used or already populated)
     * @param context the context required for some other operations (Adding the logo for example)
     * @return the newly created or updated channel list for a given user.
     */
    public static List<Channel> createOrUpdateChannelList(InputStream playlist, List<Channel> channels, Context context) {

        List<String> playlistLines = getPlaylistAsStrings(playlist);
        List<String> names = getChannelAttribute(playlistLines, Constants.ATTRIBUTE_TVG_NAME);

        /*
         Don't force unnecessary computing if the channel list is the same between the
         device and ACE's servers. Only update the EPG listing.

         In both cases, if we're using the XmlTvParser channels or that the list from the ace server
         is different, we'll have to create new ones.
         */

        if (names.size() != channels.size()) {
            return (createChannelList(playlistLines, channels, context));
        } else {
            return (updateChannelList(playlistLines, channels, context));
        }

    }

    /**
     * Gets a list of channels based on the M3U playlist of a given user.
     * @param playlist List of the playlist lines containing the user's channels
     * @param channels A complete list of already added channel. Mostly paired with channels coming from {@link XmlTvParser}
     * @param context the context required for some other operations (Adding the logo for example)
     * @return the list of channels for a given user
     */
    private static List<Channel> createChannelList(List<String> playlist, List<Channel> channels, Context context) {

        List<String> names = getChannelAttribute(playlist, Constants.ATTRIBUTE_TVG_NAME);
        List<String> links = getChannelAttribute(playlist, Constants.ATTRIBUTE_LINK);
        List<String> logos = getChannelAttribute(playlist, Constants.ATTRIBUTE_TVG_LOGO);
        List<String> ids = getChannelAttribute(playlist, Constants.ATTRIBUTE_TVG_ID);
        List<Channel> tempList = new ArrayList<>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);

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
                        channel = createChannel(tempName, Integer.toString(i + 1), tempId, tempLogo, tempLink, getProgramGenre(tempName, context));
                    } else {
                        channel = createChannel(tempName, Integer.toString(i + 1), tempId, null, tempLink, getProgramGenre(tempName, context));
                    }

                    // Premium users might have VOD content in their playlist, don't include them.
                    if (isLiveChannel(channel)) {
                        tempList.add(channel);
                    }
                    break;
                }
            }
        }
        return (tempList);
    }

    /**
     * Updates a given channel based on a given M3U playlist.
     * @param playlist the list containing each of the user's channels
     * @param channels the list of channels getting updated
     * @param context the context required for some other operations (Updating the logo for example)
     * @return the updated list of channels.
     */
    private static List<Channel> updateChannelList(List<String> playlist, List<Channel> channels, Context context) {

        List<String> logos = getChannelAttribute(playlist, Constants.ATTRIBUTE_TVG_LOGO);
        List<String> links = getChannelAttribute(playlist, Constants.ATTRIBUTE_LINK);

        for (int i = 0; i < channels.size(); i++) {
            Channel tempChannel;
            Channel currentChannel = channels.get(i);
            if (hasChannelLogo(context)) {
                tempChannel = updateChannel(currentChannel, logos.get(i), links.get(i));
            } else {
                tempChannel = updateChannel(currentChannel, null, links.get(i));
            }
            channels.set(i, tempChannel);
        }
        return (channels);
    }

    /**
     * Reads a stream from the M3U playlist from a user for easier parsing.
     * @param playlist a user's M3U playlist stream
     * @return a List containing every lines of the M3U playlist
     */
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

    /**
     * Returns a given attribute for an M3U playlist file based on it's parameter.
     * @param playlist a list containing all the user's playlist lines.
     * @param attribute an attribute as found in {@link Constants}
     * @return a list containing the given attribute for all the channels of a user's playlist.
     */
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
     * Updates a {@link Channel} registered in the Android TV's framework. It was implemented
     * in a way to alleviate the overhead of recreating channels all over again.
     *
     * @param channel The channel to update
     * @param logo the url of the logo for the channel to be shown in the guide of the Live Channels app
     * @param url the channel's video stream link
     * @return the updated channel.
     */
    private static Channel updateChannel(Channel channel, String logo, String url) {
        Channel.Builder builder = new Channel.Builder(channel);
        InternalProviderData internalProviderData = channel.getInternalProviderData();

        if (internalProviderData != null) {
            internalProviderData.setVideoUrl(url);
        }

        builder.setInternalProviderData(internalProviderData);
        builder.setChannelLogo(logo);
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
     * Determines whether has channel logos enabled on the EPG in the Live Channels app
     * @param context the context required to access the preferences
     * @return true if the user has the logos enabled.
     */
    private static boolean hasChannelLogo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);
        return (sharedPreferences.getBoolean(Constants.CHANNEL_LOGO_PREFERENCE, true));
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
