package com.zaclimon.tsutaeru.properties

import android.content.SharedPreferences
import com.google.android.media.tv.companionlibrary.model.Channel
import com.google.android.media.tv.companionlibrary.model.InternalProviderData
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
        if (channel.displayName.contains("UK") || channel.displayName.contains("IRE:")) {
            return propertiesPreferences.getBoolean(Constants.UK_REGION_PREFERENCE, true)
        } else if (channel.displayName.contains("USA/CA")) {
            return propertiesPreferences.getBoolean(Constants.NA_REGION_PREFERENCE, true)
        }

        // Don't include 24/7 channels and live event channels into the international ones.
        return channel.displayName.contains("24/7") || channel.networkAffiliation.contains("LIVE") || propertiesPreferences.getBoolean(Constants.INTERNATIONAL_REGION_PREFERENCE, true)
    }
}