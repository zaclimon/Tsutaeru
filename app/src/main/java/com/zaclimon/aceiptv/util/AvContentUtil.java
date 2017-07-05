package com.zaclimon.aceiptv.util;

import android.util.Log;
import android.util.Patterns;

import com.zaclimon.aceiptv.data.AvContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for handling {@link AvContent}
 *
 * AvContents can be handled differently for example for A.C.E. IPTV, an M3U from it's given
 * Android API is shown like this:
 *
 * #EXTINF:-1 tvg-name="" group-title="" tvg-logo="", "title"
 * "content-link"
 *
 * @author zaclimon
 * Creation date: 01/07/17
 */

public class AvContentUtil {

    private static final String LOG_TAG = "AvContentUtil";

    /**
     * Generates required {@link AvContent} for a given M3U playlist
     * @param playlist stream containing the M3U playlist
     * @return the list of AvContents from that playlist
     */
    public static List<AvContent> getAvContentsList(InputStream playlist) {

        List<String> playlistStrings = getAvContentsAsString(playlist);
        List<AvContent> avContents = new ArrayList<>();

        for (int i = 0; i < playlistStrings.size(); i++) {
            if (!Patterns.WEB_URL.matcher(playlistStrings.get(i)).matches() && i != playlistStrings.size() - 1) {
                AvContent avContent = createAvContent(playlistStrings.get(i), playlistStrings.get(i + 1));
                if (avContent != null) {
                    avContents.add(avContent);
                }
            }
        }

        return (avContents);
    }

    /**
     * Generates the groups for given {@link AvContent}
     * @param contents the list containing all the AvContents
     * @return the list of different groups for the given content
     */
    public static List<String> getAvContentsGroup(List<AvContent> contents) {

        List<String> tempGroups = new ArrayList<>();

        for (int i = 0; i < contents.size(); i++) {

             /*
             Compare two given AvContent's group and if they aren't the same, add it to the temporary
             list.

             In this case, it would be the last from a given group that will be added. The only exception
             is for the last element in case a single element from a group has been added at the end
             of the list.
             */

            AvContent tempAvContent = contents.get(i);

            if (i != contents.size() - 1 && !tempAvContent.getGroup().equals(contents.get(i + 1).getGroup())
                    || i == contents.size() - 1 && !tempAvContent.getGroup().equals(contents.get(i - 1).getGroup())) {
                tempGroups.add(tempAvContent.getGroup());
            }
        }
        return (tempGroups);
    }

    /**
     * Reads a stream from the M3U playlist from a user for easier parsing.
     * @param playlist a user's M3U playlist stream
     * @return a List containing every lines of the M3U playlist
     */
    private static List<String> getAvContentsAsString(InputStream playlist) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(playlist));
        StringBuilder stringBuilder = new StringBuilder();
        List<String> contents = new ArrayList<>();
        boolean firstCharacterRead = false;
        int characterInt;

        try {
            while ((characterInt = bufferedReader.read()) != -1) {

                char character = (char) characterInt;

                if (character == '#') {

                    if (!firstCharacterRead) {
                        firstCharacterRead = true;
                    } else {
                         /*
                          For some API's there might not be any newlines between actual lines
                          For this reason, check if there is a whitespace instead of a newline
                          before the content link section.

                          This way, we can see if it's the real link and not a letter from a content
                          title for example.
                          */
                        int contentUrlIndex = stringBuilder.toString().lastIndexOf("http://");

                        if (contentUrlIndex != -1 && Character.isWhitespace(stringBuilder.toString().charAt(contentUrlIndex - 1))) {
                            String contentUrl = stringBuilder.toString().substring(contentUrlIndex);
                            contents.add(contentUrl.trim());
                            stringBuilder.delete(contentUrlIndex, stringBuilder.length() - 1);
                        }
                        contents.add(stringBuilder.toString().trim());
                        Log.d(LOG_TAG, stringBuilder.toString().trim());
                        stringBuilder = new StringBuilder();
                    }
                }
                stringBuilder.append(character);
            }

            playlist.close();
            return (contents);
        } catch (IOException io) {
            // Couldn't read the stream
            return (null);
        }
    }

    /**
     * Creates a {@link AvContent} from a given playlist line.
     * @param playlistLine The required playlist line
     * @return the AvContent from this line
     */
    private static AvContent createAvContent(String playlistLine, String contentLink) {

        if (playlistLine.contains(Constants.ATTRIBUTE_TVG_NAME)) {
            String title = getAttributeFromPlaylistLine(Constants.ATTRIBUTE_TVG_NAME, playlistLine);
            String logo = getAttributeFromPlaylistLine(Constants.ATTRIBUTE_TVG_LOGO, playlistLine);
            String group = getAttributeFromPlaylistLine(Constants.ATTRIBUTE_GROUP_TITLE, playlistLine);
            return (new AvContent(title, logo, group, contentLink));
        } else {
            Log.e(LOG_TAG, "Current line not valid for creating AvContent: " + playlistLine);
            return (null);
        }

    }

    /**
     * Returns a given attribute for an M3U playlist file based on it's parameter.
     * @param attribute an attribute as found in {@link Constants}
     * @param playlistLine a line from a given playlist.
     * @return The attribute for that line.
     */
    private static String getAttributeFromPlaylistLine(String attribute, String playlistLine) {

        int indexAttributeStart;
        int indexAttributeEnd;

        switch (attribute) {
            case Constants.ATTRIBUTE_GROUP_TITLE:
                indexAttributeStart = playlistLine.indexOf(Constants.ATTRIBUTE_GROUP_TITLE);
                indexAttributeEnd = playlistLine.indexOf(Constants.ATTRIBUTE_TVG_LOGO);
                break;
            case Constants.ATTRIBUTE_TVG_LOGO:
                indexAttributeStart = playlistLine.indexOf(Constants.ATTRIBUTE_TVG_LOGO);
                indexAttributeEnd = playlistLine.indexOf(",");
                break;
            case Constants.ATTRIBUTE_TVG_NAME:
                // That's kinda a hack but let's use it since it seems to always be defined.
                indexAttributeStart = playlistLine.indexOf(",");
                indexAttributeEnd = playlistLine.length();
                break;
            default:
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }

        String partialAttribute = playlistLine.substring(indexAttributeStart, indexAttributeEnd);

        if (attribute.equals(Constants.ATTRIBUTE_TVG_NAME)) {
            return (partialAttribute.replace(",", ""));
        } else {
            // We're using pure M3U attributes
            String[] partialAttributeParts = partialAttribute.split("=");
            return (partialAttributeParts[1].replace("\"", "").trim());
        }

    }
}
