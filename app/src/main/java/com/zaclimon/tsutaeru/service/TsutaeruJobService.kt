package com.zaclimon.tsutaeru.service

import android.app.job.JobParameters
import android.content.Context
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.properties.TsutaeruChannelProperties
import com.zaclimon.tsutaeru.util.Constants
import com.zaclimon.xipl.properties.ChannelProperties
import com.zaclimon.xipl.service.ProviderEpgService

/**
 * Custom [ProviderEpgService] adapted for syncing a Tsutaeru provider content.
 * <p>
 * It downloads and parses the content from a user's M3U playlist + EPG XMLTV to add it to the
 * Android TV's system database in order to be used by the Live Channels application.
 *
 * @author zaclimon
 */
class TsutaeruJobService : ProviderEpgService() {

    private lateinit var serviceProperties: ChannelProperties

    override fun onStartJob(params: JobParameters?): Boolean {
        serviceProperties = TsutaeruChannelProperties(getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE))
        return super.onStartJob(params)
    }

    override fun getPlaylistUrl(): String {
        val sharedPreferences = getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val url = sharedPreferences?.getString(Constants.PROVIDER_URL_PREFERENCE, "")
        val username = sharedPreferences?.getString(Constants.USERNAME_PREFERENCE, "")
        val password = sharedPreferences?.getString(Constants.PASSWORD_PREFERENCE, "")
        val streamType = sharedPreferences?.getString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS)
        return getString(R.string.provider_playlist_url, url, username, password, streamType)
    }

    override fun getEpgUrl(): String {
        val sharedPreferences = getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val url = sharedPreferences?.getString(Constants.PROVIDER_URL_PREFERENCE, "")
        val username = sharedPreferences?.getString(Constants.USERNAME_PREFERENCE, "")
        val password = sharedPreferences?.getString(Constants.PASSWORD_PREFERENCE, "")
        return getString(R.string.provider_epg_url, url, username, password)
    }

    override fun getChannelProperties(): ChannelProperties {
        return serviceProperties
    }

}