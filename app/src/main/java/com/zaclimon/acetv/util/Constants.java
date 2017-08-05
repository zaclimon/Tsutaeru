package com.zaclimon.acetv.util;

import android.media.tv.TvContract;

import java.util.regex.Pattern;

/**
 * List of constants to be used throughout the application's lifecycle.
 *
 * @author zaclimon
 * Creation date: 17/06/17
 */

public class Constants {

    // M3U file attributes
    public static final Pattern ATTRIBUTE_TVG_ID_PATTERN = Pattern.compile("tvg-id.\"(.*?)\"");
    public static final Pattern ATTRIBUTE_TVG_LOGO_PATTERN = Pattern.compile("tvg-logo.\"(.*?)\"");
    public static final Pattern ATTRIBUTE_TVG_NAME_PATTERN = Pattern.compile("tvg-name.\"(.*?)\"");
    public static final Pattern ATTRIBUTE_GROUP_TITLE_PATTERN = Pattern.compile("group-title.\"(.*?)\"");

    // Preferences stuff
    public static final String ACE_TV_PREFERENCES = "AceSharedPreferences";
    public static final String USERNAME_PREFERENCE = "username";
    public static final String PASSWORD_PREFERENCE = "password";
    public static final String STREAM_TYPE_PREFERENCE = "stream_type";
    public static final String STREAM_TYPE_MPEG_TS = "ts";
    public static final String STREAM_TYPE_HLS = "hls";
    public static final String CHANNEL_LOGO_PREFERENCE = "channel_logo";
    public static final String VIDEO_FIT_SCREEN_PREFERENCE = "video_fit_screen";

    // Channel configuration stuff
    public static final String EPG_ID_PROVIDER = "epg_id";
    public static final String CHANNEL_GENRES_PROVIDER = "channel_genres";
    public static final String[] CHANNEL_GENRES = new String[]{
            TvContract.Programs.Genres.ANIMAL_WILDLIFE,
            TvContract.Programs.Genres.ARTS,
            TvContract.Programs.Genres.COMEDY,
            TvContract.Programs.Genres.DRAMA,
            TvContract.Programs.Genres.EDUCATION,
            TvContract.Programs.Genres.ENTERTAINMENT,
            TvContract.Programs.Genres.FAMILY_KIDS,
            TvContract.Programs.Genres.GAMING,
            TvContract.Programs.Genres.LIFE_STYLE,
            TvContract.Programs.Genres.MOVIES,
            TvContract.Programs.Genres.MUSIC,
            TvContract.Programs.Genres.NEWS,
            TvContract.Programs.Genres.PREMIER,
            TvContract.Programs.Genres.SHOPPING,
            TvContract.Programs.Genres.SPORTS,
            TvContract.Programs.Genres.TECH_SCIENCE,
            TvContract.Programs.Genres.TRAVEL
    };

    // App update stuff
    public static final String ACE_BASE_URL = "https://quantaworks.io/public/acetv/";
    public static final String ACE_CHANGELOG_URL = ACE_BASE_URL + "updater/changelog.json";
    public static final String ACE_RELEASES_URL = ACE_BASE_URL + "releases/";
}
