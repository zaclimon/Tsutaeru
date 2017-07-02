package com.zaclimon.aceiptv.util;

import android.util.Log;

import com.zaclimon.aceiptv.data.AvContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by isaac on 17-07-01.
 */

public class AvContentUtil {

    private static final String LOG_TAG = "AvContentUtil";

    public static List<AvContent> getAvContentsList(InputStream playlist) {

        List<String> playlistStrings = getAvContentsAsString(playlist);
        List<AvContent> avContents = new ArrayList<>();

        for (String content : playlistStrings) {
            AvContent avContent = createAvContent(content);
            if (avContent != null) {
                avContents.add(avContent);
            }
        }
        return (avContents);
    }

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

    private static List<String> getAvContentsAsString(InputStream playlist) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(playlist));
        StringBuilder stringBuilder = new StringBuilder();
        List<String> contents = new ArrayList<>();
        boolean firstCharacterRead = false;
        int character;

        try {
            while ((character = bufferedReader.read()) != -1) {

                if ((char) character == '#') {

                    if (!firstCharacterRead) {
                        firstCharacterRead = true;
                    } else {
                        contents.add(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                    }
                }
                stringBuilder.append((char) character);
            }

            playlist.close();
            return (contents);
        } catch (IOException io) {
            // Couldn't read the stream
            return (null);
        }
    }

    private static AvContent createAvContent(String playlistLine) {

        if (playlistLine.contains("#EXTINF:-1 tvg-name=\"\"")) {
            String title = getAttributeFromPlaylistLine(Constants.ATTRIBUTE_TVG_NAME, playlistLine);
            String logo = getAttributeFromPlaylistLine(Constants.ATTRIBUTE_TVG_LOGO, playlistLine);
            String group = getAttributeFromPlaylistLine(Constants.ATTRIBUTE_GROUP_TITLE, playlistLine);
            String link = getAttributeFromPlaylistLine(Constants.ATTRIBUTE_LINK, playlistLine);
            return (new AvContent(title, logo, group, link));
        } else {
            Log.e(LOG_TAG, "Current line not valid for creating AvContent: " + playlistLine);
            return (null);
        }

    }

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
                // That's kinda a hack but let's use it since it is not set anyway.
                indexAttributeStart = playlistLine.indexOf(",");
                indexAttributeEnd = playlistLine.lastIndexOf("http");
                break;
            case Constants.ATTRIBUTE_LINK:
                indexAttributeStart = playlistLine.lastIndexOf("http");
                indexAttributeEnd = playlistLine.length() - 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }

        String partialAttribute = playlistLine.substring(indexAttributeStart, indexAttributeEnd);
        String[] partialAttributeParts = partialAttribute.split("=");
        if (partialAttributeParts.length > 1) {
            // We're using pure M3U attributes
            return (partialAttributeParts[1].replace("\"", "").trim());
        } else {
            // We're most likely retrieving a content link.
            return (partialAttribute.replace(",", ""));
        }

    }
}
