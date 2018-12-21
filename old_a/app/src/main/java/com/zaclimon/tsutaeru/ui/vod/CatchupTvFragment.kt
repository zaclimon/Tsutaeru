package com.zaclimon.tsutaeru.ui.vod

import android.content.Context
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.util.Constants

/**
 * Fragment responsible for showing Tv Catchup content for a compatible Tsutaeru provider.
 *
 * @author zaclimon
 */
class CatchupTvFragment : TsutaeruVodTvSectionFragment() {

    override fun getVodContentApiLink(): String {
        val sharedPreferences = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val url = sharedPreferences?.getString(Constants.PROVIDER_URL_PREFERENCE, "")
        val username = sharedPreferences?.getString(Constants.USERNAME_PREFERENCE, "")
        val password = sharedPreferences?.getString(Constants.PASSWORD_PREFERENCE, "")
        val offset = sharedPreferences?.getInt(Constants.EPG_OFFSET_PREFERENCE, 0)

        if (offset != 0) {
            return getString(R.string.provider_catchup_url, url, username, password)
        }

        return getString(R.string.provider_catchup_offset_url, url, offset, username, password)
    }

}