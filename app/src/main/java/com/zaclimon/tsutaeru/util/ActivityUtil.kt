package com.zaclimon.tsutaeru.util

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration

/**
 * Utility class reserved designed but not reserved around Android activities.
 *
 * @author zaclimon
 */
class ActivityUtil {

    companion object {
        /**
         * Verifies if there is not a provider url/username/password match from within the application.
         *
         * @param context the required context to verify the url/username/password
         * @return true if there is not a provider url or a username or a password.
         */
        fun areCredentialsEmpty(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
            val url = sharedPreferences.getString(Constants.PROVIDER_URL_PREFERENCE, "")
            val username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "")
            val password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "")
            return (url.isEmpty() || username.isEmpty() || password.isEmpty())
        }

        /**
         * Verifies if the current user interface (UI) mode is for television (Mostly if we're in
         * Android TV)
         *
         * @param activity the activity verifying the UI mode.
         * @return true if the application is running in Android TV.
         */
        fun isTvMode(activity: Activity): Boolean {
            val uiModeManager = activity.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            return (uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION)
        }
    }

}