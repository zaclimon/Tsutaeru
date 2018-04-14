package com.zaclimon.tsutaeru.ui.auth

import android.os.AsyncTask
import android.util.Patterns
import com.zaclimon.tsutaeru.repository.SharedPreferencesRepository
import com.zaclimon.tsutaeru.util.Constants
import com.zaclimon.tsutaeru.util.NetworkUtils
import java.io.FileNotFoundException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * AsyncTask class that verifies the given credentials of a given user.
 * <p>
 * Note that for it to be used, a [AuthView] and a [SharedPreferencesRepository]
 * are required since callback to both of them will be made.
 *
 * @property taskUrl the user's provider URL
 * @property taskAuthView the AuthView in which callbacks will be called
 * @property taskSharedPreferencesRepository the SharedPreferencesRepository used to save the credentials
 *
 * @author zaclimon
 */
class AsyncAuthValidateInfo(url: String,
                            authView: AuthView,
                            sharedPreferencesRepository: SharedPreferencesRepository) : AsyncTask<Void, Void, Boolean>() {

    private var taskUrl = url
    private lateinit var taskUsername: String
    private lateinit var taskPassword: String
    private val taskAuthView = authView
    private val taskSharedPreferencesRepository = sharedPreferencesRepository
    private var ioException: IOException? = null

    override fun doInBackground(vararg p0: Void?): Boolean {

        if (!taskUrl.startsWith("http://")) {
            taskUrl = "http://$taskUrl"
        }

        taskUrl = taskUrl.removeSuffix("/")

        if (isValidURL(taskUrl)) {
            try {
                val reader = NetworkUtils.getNetworkInputStream(taskUrl).bufferedReader()
                val playlistFirstLine = reader.readLine()

                if (playlistFirstLine != "#EXTM3U") {
                    return false
                }

                val usernameMatcher = Regex("username=.+?(?=&)").find(taskUrl)
                val passwordMatcher = Regex("password=.+?(?=&)").find(taskUrl)
                val urlMatcher = Regex("http://[^/]*").find(taskUrl)

                if (usernameMatcher != null && passwordMatcher != null && urlMatcher != null) {
                    taskUsername = usernameMatcher.value.removePrefix("username=")
                    taskPassword = passwordMatcher.value.removePrefix("password=")
                    taskUrl = urlMatcher.value
                    reader.close()
                } else {
                    return false
                }

            } catch (io: IOException) {
                ioException = io
                io.printStackTrace()
                return false
            }
            return true
        }
        return false
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)

        if (result != null && result) {
            taskSharedPreferencesRepository.putString(Constants.PROVIDER_URL_PREFERENCE, taskUrl)
            taskSharedPreferencesRepository.putString(Constants.USERNAME_PREFERENCE, taskUsername)
            taskSharedPreferencesRepository.putString(Constants.PASSWORD_PREFERENCE, taskPassword)
            taskSharedPreferencesRepository.apply()
            taskAuthView.onConnectionSuccess()
        } else if (result != null && ioException is FileNotFoundException) {
            taskAuthView.onConnectionFailed()
        } else if (result != null && ioException is SocketTimeoutException) {
            taskAuthView.onTimeoutReceived()
        } else if (result != null) {
            // If anything else received is not a valid M3U playlist
            taskAuthView.onConnectionFailed()
        }
    }

    private fun isValidURL(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

}