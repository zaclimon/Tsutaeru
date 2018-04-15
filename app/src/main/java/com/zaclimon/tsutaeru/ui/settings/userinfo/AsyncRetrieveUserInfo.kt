package com.zaclimon.tsutaeru.ui.settings.userinfo

import android.os.AsyncTask
import com.zaclimon.tsutaeru.util.NetworkUtils
import org.json.JSONObject
import java.util.*

/**
 * Async class that will retrieve a given user's account information.
 *
 * @author zaclimon
 */
class AsyncRetrieveUserInfo(userEndpoint: String, view: UserInfoView) : AsyncTask<Void, Void, Boolean>() {

    companion object {
        private const val USER_INFO_JSON_OBJECT = "user_info"
        private const val STATUS_JSON_OBJECT = "status"
        private const val EXPIRATION_DATE_JSON_OBJECT = "exp_date"
        private const val TRIAL_ACCOUNT_JSON_OBJECT = "is_trial"
        private const val MAX_CONNECTION_JSON_OBJECT = "max_connections"
    }

    private val apiEndpoint = userEndpoint
    private val userInfoView = view
    private var status: String? = null
    private var expirationDate: Date? = null
    private var isTrial: Boolean = false
    private var maxConnections: Int = 0

    override fun doInBackground(vararg p0: Void?): Boolean {

        try {
            val inputStream = NetworkUtils.getNetworkInputStream(apiEndpoint)
            val reader = inputStream.bufferedReader()
            val jsonString = reader.readLine()

            if (!jsonString.isNullOrEmpty()) {
                val baseJsonObject = JSONObject(jsonString)
                val realJsonObject = baseJsonObject.getJSONObject(USER_INFO_JSON_OBJECT)
                val calendar = Calendar.getInstance()

                status = realJsonObject.getString(STATUS_JSON_OBJECT)

                /*
                 The returned date is in unix time, which is in seconds since the January 1st 1970.

                 The time zone as set in the original Ace TV application was set according to
                 GMT +2 (Central european/CET as defined now) let's set it according to the user's
                 actual time zone.
                 */

                val expirationDateSeconds = realJsonObject.getLong(EXPIRATION_DATE_JSON_OBJECT)
                calendar.timeInMillis = (expirationDateSeconds * 1000)
                expirationDate = calendar.time

                isTrial = (realJsonObject.getInt(TRIAL_ACCOUNT_JSON_OBJECT) == 1)
                maxConnections = realJsonObject.getInt(MAX_CONNECTION_JSON_OBJECT)
                inputStream.close()
                return true
            }
        } catch (ex: Exception) {
            // Nothing we can do really...
        }
        return false
    }

    override fun onPostExecute(result: Boolean?) {
        if (result != null && result) {
            userInfoView.onConnectionSuccess(status!!, expirationDate!!, isTrial, maxConnections)
        } else if (result != null) {
            userInfoView.onConnectionFailed()
        }
    }
}