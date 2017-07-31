package com.zaclimon.acetv.util;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.UI_MODE_SERVICE;

/**
 * Utility class most likely to be used by activities.
 *
 * @author zaclimon
 * Creation date: 25/06/17
 */

public class ActivityUtil {

    /**
     * Verifies if there is not a username/password match from within the application.
     *
     * @param context the required context to verify the username/password
     * @return true if there is not a username or a password.
     */
    public static boolean isUsernamePasswordEmpty(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.ACE_TV_PREFERENCES, MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");

        return (TextUtils.isEmpty(username) || TextUtils.isEmpty(password));
    }

    /**
     * Verifies if the current user interface (UI) mode is for television (Mostly if we're in
     * Android TV)
     *
     * @param activity the activity verifying the UI mode.
     * @return true if the application is running in Android TV.
     */
    public static boolean isTvMode(Activity activity) {
        UiModeManager uiModeManager = (UiModeManager) activity.getSystemService(UI_MODE_SERVICE);
        return (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION);
    }

    /**
     * Converts given density-independent pixels (dp) to pixels
     *
     * @param dp      the number of dp to convert
     * @param context the required context to convert the dp
     * @return the number of pixel for the given dp.
     */
    public static int dpToPixel(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return ((int) (dp * density + 0.5f));
    }

    /**
     * Determines whether a given video playback should entirely fit to the screen
     *
     * @param context the context required to determine the preference
     * @return true if the user wants the video content to fit to the screen
     */
    public static boolean isVideoFitToScreen(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.ACE_TV_PREFERENCES, MODE_PRIVATE);
        return (sharedPreferences.getBoolean(Constants.VIDEO_FIT_SCREEN_PREFERENCE, false));
    }

}
