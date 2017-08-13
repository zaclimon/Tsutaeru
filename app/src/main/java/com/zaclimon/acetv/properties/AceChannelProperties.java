package com.zaclimon.acetv.properties;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.InternalProviderData;
import com.zaclimon.acetv.util.Constants;
import com.zaclimon.xipl.properties.ChannelProperties;

/**
 * Concrete implementation of {@link ChannelProperties} for Ace TV.
 *
 * @author zaclimon
 * Creation date: 13/08/17
 */

public class AceChannelProperties implements ChannelProperties {

    private SharedPreferences mSharedPreferences;

    public AceChannelProperties(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public boolean hasChannelLogo() {
        return (mSharedPreferences.getBoolean(Constants.CHANNEL_LOGO_PREFERENCE, true));
    }

    @Override
    public boolean isLiveChannel(Channel channel) {
        InternalProviderData internalProviderData = channel.getInternalProviderData();
        return (internalProviderData != null && internalProviderData.getVideoUrl().contains("/live/"));
    }

    @Override
    public boolean isChannelRegionValid(Channel channel) {

        Log.d(getClass().getSimpleName(), "Channel name: " + channel.getDisplayName());

        if (channel.getDisplayName().contains("UK:") || channel.getDisplayName().contains("IRE:")) {
            return (mSharedPreferences.getBoolean(Constants.UK_REGION_PREFERENCE, true));
        } else if (channel.getDisplayName().contains("USA/CA:")) {
            return (mSharedPreferences.getBoolean(Constants.NA_REGION_PREFERENCE, true));
        } else {
            // Don't include 24/7 channels and live event channels into the international ones.
            return (channel.getDisplayName().contains("24/7") || channel.getNetworkAffiliation().contains("LIVE") || mSharedPreferences.getBoolean(Constants.INTERNATIONAL_REGION_PREFERENCE, true));
        }
    }

}
