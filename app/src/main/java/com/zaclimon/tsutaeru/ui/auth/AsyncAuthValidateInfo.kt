package com.zaclimon.tsutaeru.ui.auth

import android.os.AsyncTask
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

        try {
            val reader = NetworkUtils.getNetworkInputStream(taskUrl).bufferedReader()
            val playlistFirstLine = reader.readLine()

            if (playlistFirstLine != "#EXTM3U") {
                return (false)
            }

            taskUsername = taskUrl.split("username=.+?(?=\\&)")[0].removePrefix("username=")
            taskPassword = taskUrl.split("password=.+?(?=\\&)")[0].removePrefix("password=")
            taskUrl = taskUrl.split("url=.+?(?=\\&)")[0].removePrefix("url=")
            reader.close()
        } catch (io: IOException) {
            ioException = io
            return (false)
        }
        return (true)
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
}