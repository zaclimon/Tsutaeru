package com.zaclimon.tsutaeru.util

import android.content.ComponentName
import com.zaclimon.xipl.Constants
import com.zaclimon.xipl.service.ProviderTvInputService

/**
 * List of constants to be used throughout the application's lifecycle.
 *
 * @author zaclimon
 */

object Constants {

    /**
     * [ComponentName] used for defining easily an input id for the application
     */
    val TV_INPUT_SERVICE_COMPONENT = ComponentName("com.zaclimon.tsutaeru", ProviderTvInputService::class.java.canonicalName)

    // Preferences stuff
    const val TSUTAERU_PREFERENCES = "TsutaeruSharedPreferences"
    const val PROVIDER_URL_PREFERENCE = "provider_url"
    const val USERNAME_PREFERENCE = "username"
    const val PASSWORD_PREFERENCE = "password"
    const val STREAM_TYPE_PREFERENCE = "stream_type"
    const val STREAM_TYPE_MPEG_TS = "mpeg_ts"
    const val STREAM_TYPE_HLS = "hls"
    const val CHANNEL_LOGO_PREFERENCE = "channel_logo"
    const val VIDEO_FIT_SCREEN_PREFERENCE = "video_fit_screen"
    const val UK_REGION_PREFERENCE = "uk_region"
    const val NA_REGION_PREFERENCE = "na_region"
    const val INTERNATIONAL_REGION_PREFERENCE = "int_region"
    const val EPG_OFFSET_PREFERENCE = "epg_offset"
    const val EXTERNAL_PLAYER_PREFERENCE = "external_player"
    const val CHANNEL_GENRE_PREFERENCE = "genre_"
    const val CHANNEL_GROUP_PREFERENCE = "group_"

    // xipl stuff
    val CHANNEL_GENRES: Array<String> = Constants.CHANNEL_GENRES
    const val CHANNEL_GENRES_PROVIDER: String = Constants.CHANNEL_GENRES_PROVIDER

}