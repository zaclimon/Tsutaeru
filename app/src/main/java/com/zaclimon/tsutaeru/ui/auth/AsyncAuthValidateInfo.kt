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
                NetworkUtils.getNetworkInputStream(taskUrl).bufferedReader().use {
                    reader -> if (!reader.readLine().contains("#EXTM3U")) return false
                }

                val usernameMatcher = Regex("username=.+?(?=&)").find(taskUrl)
                val passwordMatcher = Regex("password=.+?(?=&)").find(taskUrl)
                val urlMatcher = Regex("http://[^/]*").find(taskUrl)

                if (usernameMatcher != null && passwordMatcher != null && urlMatcher != null) {
                    taskUsername = usernameMatcher.value.removePrefix("username=")
                    taskPassword = passwordMatcher.value.removePrefix("password=")
                    taskUrl = urlMatcher.value
                } else {
                    return false
                }

                return true
            } catch (io: IOException) {
                ioException = io
                io.printStackTrace()
            }
        }
        return false
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        when {
            result -> {
                taskSharedPreferencesRepository.putString(Constants.PROVIDER_URL_PREFERENCE, taskUrl)
                taskSharedPreferencesRepository.putString(Constants.USERNAME_PREFERENCE, taskUsername)
                taskSharedPreferencesRepository.putString(Constants.PASSWORD_PREFERENCE, taskPassword)
                taskAuthView.onConnectionSuccess(areCredentialsDifferent(taskUrl, taskUsername, taskPassword))
                taskSharedPreferencesRepository.apply()
            }
            ioException is FileNotFoundException -> taskAuthView.onConnectionFailed()
            ioException is SocketTimeoutException -> taskAuthView.onTimeoutReceived()
            // Anything else received is not a valid M3U playlist
            else -> taskAuthView.onConnectionFailed()
        }
    }

    /**
     * Validates if a URL is valid for further use.
     *
     * @param url the URL that is getting verified
     * @return true if the URL is a valid one
     */
    private fun isValidURL(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    /**
     * Verifies if the credentials of an account are different compared to another one.
     *
     * @param providerUrl the url of the provider for the account
     * @param username the username of the account
     * @param password the password of the account
     * @return true if the url, username, and password are different from the registered ones.
     */
    private fun areCredentialsDifferent(providerUrl: String, username: String, password: String): Boolean {
        val initialProvider = taskSharedPreferencesRepository.getString(Constants.PROVIDER_URL_PREFERENCE)
        val initialUserName = taskSharedPreferencesRepository.getString(Constants.USERNAME_PREFERENCE)
        val initialPassword = taskSharedPreferencesRepository.getString(Constants.PASSWORD_PREFERENCE)
        return (initialProvider != providerUrl || initialUserName != username || initialPassword != password)
    }

}