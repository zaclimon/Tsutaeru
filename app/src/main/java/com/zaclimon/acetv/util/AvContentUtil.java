package com.zaclimon.acetv.util;

import android.util.Log;
import android.util.Patterns;

import com.zaclimon.acetv.data.AvContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Utility class responsible for handling {@link AvContent}
 *
 * AvContents can be handled differently for example for Ace TV, an M3U from it's given
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
            if (!Patterns.WEB_URL.matcher(playlistStrings.get(i)).matches() && !playlistStrings.get(i).contains("#EXTM3U")) {
                // The next line is guaranteed to be the content link.
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

        HashSet<String> tempGroups = new HashSet<>();

        /*
         Some values might not be sorted equally and as a result, there might be duplicates in
         the list. Use HashSet to not include any of them.
         */

        for (int i = 0; i < contents.size(); i++) {
             tempGroups.add(contents.get(i).getGroup());
        }
        return (new ArrayList<>(tempGroups));
    }

    /**
     * Reads a stream from the M3U playlist from a user for easier parsing.
     * @param playlist a user's M3U playlist stream
     * @return a List containing every lines of the M3U playlist
     */
    private static List<String> getAvContentsAsString(InputStream playlist) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(playlist));
        List<String> contents = new ArrayList<>();
        String tempLine;

        try {
            while ((tempLine = bufferedReader.readLine()) != null) {
                contents.add(tempLine);
            }

            if (contents.size() == 1) {
                return (generatePlaylistLinesSingle(contents.get(0)));
            } else {
                return (generatePlaylistLinesMulti(contents));
            }
        } catch (IOException io) {
            // Couldn't read the stream
            return (null);
        }
    }

    /**
     * Generates the required M3U playlist lines if the given playlist was on a single line (That is
     * without a new line)
     * @param playlist the whole playlist.
     * @return A list containing all the lines of the M3U playlist.
     */
    private static List<String> generatePlaylistLinesSingle(String playlist) {

        /*
         For some API's there might not be any newlines between actual lines
         For this reason, check if there is a whitespace instead of a newline
         before the content link section.

         This way, we can see if it's the real link and not a letter from a content
         title for example.
        */

        List<String> contents = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        boolean firstCharacterRead = false;

        for (int i = 0; i < playlist.length(); i++) {
            char character = playlist.charAt(i);

            if (character == '#' && firstCharacterRead) {
                String currentString = stringBuilder.toString();
                int contentUrlIndex = currentString.lastIndexOf("http://");

                if (contentUrlIndex != -1 && Character.isWhitespace(currentString.charAt(contentUrlIndex - 1))) {
                    String contentUrl = stringBuilder.toString().substring(contentUrlIndex);
                    contents.add(contentUrl.trim());
                    stringBuilder.delete(contentUrlIndex, stringBuilder.length() - 1);
                    contents.add(stringBuilder.toString().trim());
                    stringBuilder = new StringBuilder();
                }
            } else {
                firstCharacterRead = true;
            }
            stringBuilder.append(character);
        }
        return (contents);
    }

    /**
     * Generates all the required lines from a given playlist if that playlist had multiple lines.
     * @param playlist the playlist as a list separated by it's lines
     * @return a list having the required M3U playlist lines.
     */
    private static List<String> generatePlaylistLinesMulti(List<String> playlist) {

        List<String> contents = new ArrayList<>();

        for (String playlistLine : playlist) {
            if (playlistLine.startsWith("#EXTINF") || playlistLine.startsWith("http://")) {
                contents.add(playlistLine);
            }
        }
        return (contents);
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
                // Some titles can have "," in them. Start from the logo attribute in this case.
                indexAttributeStart = playlistLine.indexOf(Constants.ATTRIBUTE_TVG_LOGO);
                indexAttributeEnd = playlistLine.indexOf(",", indexAttributeStart);
                break;
            case Constants.ATTRIBUTE_TVG_NAME:
                 /*
                  That's kinda a hack but let's use it since it seems to always be defined. Since
                  some names have "," in them, let's begin from the end of the logo. We're sure to
                  find a name there.
                  */
                int indexEndOfLogo = playlistLine.indexOf(",", playlistLine.indexOf(Constants.ATTRIBUTE_TVG_LOGO));
                indexAttributeStart = playlistLine.indexOf(",", indexEndOfLogo);
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
