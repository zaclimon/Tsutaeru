package com.zaclimon.acetv.properties;

import android.content.SharedPreferences;

import com.zaclimon.acetv.util.Constants;
import com.zaclimon.xipl.properties.VodProperties;

/**
 * {@link VodProperties} applicable for Ace TV.
 *
 * @author zaclimon
 * Creation date: 13/08/17
 */

public class AceVodPlaybackProperties implements VodProperties {

    private SharedPreferences mSharedPreferences;

    public AceVodPlaybackProperties(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public boolean isVideoFitToScreen() {
        return (mSharedPreferences.getBoolean(Constants.VIDEO_FIT_SCREEN_PREFERENCE, false));
    }

    @Override
    public boolean isExternalPlayerUsed() {
        return (mSharedPreferences.getBoolean(Constants.EXTERNAL_PLAYER_PREFERENCE, false));
    }

}
