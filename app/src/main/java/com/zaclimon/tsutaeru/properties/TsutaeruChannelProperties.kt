package com.zaclimon.tsutaeru.properties

import android.content.SharedPreferences
import com.google.android.media.tv.companionlibrary.model.Channel
import com.zaclimon.tsutaeru.util.Constants
import com.zaclimon.xipl.properties.ChannelProperties
import com.zaclimon.xipl.util.ProviderChannelUtil

/**
 * Concrete implementation of [ChannelProperties] for Tsutaeru.
 *
 * @author zaclimon
 */
class TsutaeruChannelProperties(sharedPreferences: SharedPreferences) : ChannelProperties {

    private val propertiesPreferences = sharedPreferences

    override fun hasChannelLogo(): Boolean {
        return propertiesPreferences.getBoolean(Constants.CHANNEL_LOGO_PREFERENCE, true)
    }

    override fun isLiveChannel(channel: Channel): Boolean {
        val internalProviderData = channel.internalProviderData
        return internalProviderData !=  null && internalProviderData.videoUrl.contains("/live/")
    }

    override fun isChannelGenreValid(channel: Channel): Boolean {
        val providerData = channel.internalProviderData

        providerData?.let {
            val genres = it.get(Constants.CHANNEL_GENRES_PROVIDER) as String
            val genresArray = ProviderChannelUtil.getGenresArrayFromJson(genres)

            for (channelGenre in genresArray) {
                if (!propertiesPreferences.getBoolean(Constants.CHANNEL_GENRE_PREFERENCE + channelGenre, true)) {
                    return false
                }
            }
        }
        return true
    }

    override fun isChannelRegionValid(channel: Channel): Boolean {
       return true
    }

    override fun isChannelGroupValid(channel: Channel): Boolean {
        return propertiesPreferences.getBoolean(Constants.CHANNEL_GROUP_PREFERENCE + channel.networkAffiliation, true)
    }
}