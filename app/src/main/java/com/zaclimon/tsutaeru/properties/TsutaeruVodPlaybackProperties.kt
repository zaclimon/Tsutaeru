package com.zaclimon.tsutaeru.properties

import android.content.SharedPreferences
import com.zaclimon.tsutaeru.util.Constants
import com.zaclimon.xipl.properties.VodProperties

/**
 * [VodProperties] concrete implementation for Tsutaeru.
 *
 * @author zaclimon
 */
class TsutaeruVodPlaybackProperties(sharedPreferences: SharedPreferences) : VodProperties {

    private val propertiesPreferences = sharedPreferences

    override fun isVideoFitToScreen(): Boolean {
        return propertiesPreferences.getBoolean(Constants.VIDEO_FIT_SCREEN_PREFERENCE, false)
    }

    override fun isExternalPlayerUsed(): Boolean {
        return propertiesPreferences.getBoolean(Constants.EXTERNAL_PLAYER_PREFERENCE, false)
    }

}