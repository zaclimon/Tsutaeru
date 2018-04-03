package com.zaclimon.tsutaeru.ui.playback

import android.content.Context
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.properties.TsutaeruVodPlaybackProperties
import com.zaclimon.tsutaeru.util.Constants
import com.zaclimon.xipl.properties.VodProperties
import com.zaclimon.xipl.ui.vod.VodPlaybackFragment

/**
 * Fragment responsible for playing a given VOD content.
 *
 * @author zaclimon
 */
class TsutaeruPlaybackFragment : VodPlaybackFragment() {

    override fun getProviderName(): String {
        return (getString(R.string.app_name))
    }

    override fun getVodProperties(): VodProperties {
        return (TsutaeruVodPlaybackProperties(activity!!.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)))
    }
}