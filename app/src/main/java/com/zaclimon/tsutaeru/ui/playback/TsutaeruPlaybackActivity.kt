package com.zaclimon.tsutaeru.ui.playback

import com.zaclimon.tsutaeru.R
import com.zaclimon.xipl.ui.vod.VodPlaybackActivity
import com.zaclimon.xipl.ui.vod.VodPlaybackFragment

/**
 * Activity responsible of playing a given VOD content. It is a concrete implementation of
 * [VodPlaybackActivity] for Tsutaeru.
 *
 * @author zaclimon
 */
class TsutaeruPlaybackActivity : VodPlaybackActivity() {

    override fun getThemeId(): Int {
        return R.style.TvTheme
    }

    override fun getVodPlaybackFragment(): VodPlaybackFragment {
        return TsutaeruPlaybackFragment()
    }
}