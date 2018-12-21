package com.zaclimon.tsutaeru.ui.vod

import android.content.Context
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.util.Constants

/**
 * Fragment responsible for showing VOD content for Tsutaeru providers.
 *
 * @author zaclimon
 */
class VodTvFragment : TsutaeruVodTvSectionFragment() {

    override fun getVodContentApiLink(): String {
        val sharedPreferences = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val url = sharedPreferences?.getString(Constants.PROVIDER_URL_PREFERENCE, "")
        val username = sharedPreferences?.getString(Constants.USERNAME_PREFERENCE, "")
        val password = sharedPreferences?.getString(Constants.PASSWORD_PREFERENCE, "")
        return getString(R.string.provider_vod_url, url, username, password)
    }

}