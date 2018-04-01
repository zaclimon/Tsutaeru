package com.zaclimon.tsutaeru.ui.auth

import android.os.AsyncTask
import android.util.Log
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
class AsyncAuthValidateInfo(url: String?,
                            authView: AuthView,
                            sharedPreferencesRepository: SharedPreferencesRepository) : AsyncTask<Void, Void, Boolean>() {

    private val taskUrl = url
    private val taskAuthView = authView
    private val taskSharedPreferencesRepository = sharedPreferencesRepository
    private var ioException: IOException? = null

    override fun doInBackground(vararg p0: Void?): Boolean {

        try {
            NetworkUtils.getNetworkInputStream(taskUrl)
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
            taskAuthView.onConnectionSuccess()
        } else if (result != null && ioException is FileNotFoundException) {
            taskAuthView.onConnectionFailed()
        } else if (result != null && ioException is SocketTimeoutException) {
            taskAuthView.onTimeoutReceived()
        }
    }
}