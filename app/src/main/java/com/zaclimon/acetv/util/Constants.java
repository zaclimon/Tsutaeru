package com.zaclimon.acetv.util;

import android.content.ComponentName;
import android.media.tv.TvContract;

import com.zaclimon.xipl.service.ProviderTvInputService;

import java.util.regex.Pattern;

/**
 * List of constants to be used throughout the application's lifecycle.
 *
 * @author zaclimon
 * Creation date: 17/06/17
 */

public class Constants {

    /**
     * {@link ComponentName} used for defining easily an input id for the application
     */
    public static final ComponentName TV_INPUT_SERVICE_COMPONENT = new ComponentName("com.zaclimon.acetv", ProviderTvInputService.class.getName());

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
    public static final String UK_REGION_PREFERENCE = "uk_region";
    public static final String NA_REGION_PREFERENCE = "na_region";
    public static final String INTERNATIONAL_REGION_PREFERENCE = "int_region";

    // App update stuff
    public static final String ACE_BASE_URL = "https://quantaworks.io/public/acetv/";
    public static final String ACE_CHANGELOG_URL = ACE_BASE_URL + "updater/changelog.json";
    public static final String ACE_RELEASES_URL = ACE_BASE_URL + "releases/";
}
