package com.zaclimon.aceiptv.util;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.UI_MODE_SERVICE;

/**
 * Created by isaac on 17-06-25.
 */

public class Utilities {

    public static boolean isUsernamePasswordEmpty(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.ACE_IPTV_PREFERENCES, MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");

        return (TextUtils.isEmpty(username) || TextUtils.isEmpty(password));
    }

    public static boolean isTvMode(Activity activity) {
        UiModeManager uiModeManager = (UiModeManager) activity.getSystemService(UI_MODE_SERVICE);
        return (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION);
    }

}
