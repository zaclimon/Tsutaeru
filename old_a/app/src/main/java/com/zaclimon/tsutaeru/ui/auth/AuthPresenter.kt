package com.zaclimon.tsutaeru.ui.auth

import com.zaclimon.tsutaeru.repository.SharedPreferencesRepository

/**
 * Interface that defines a valid Presenter used for the authentication process.
 *
 * @author zaclimon
 */
interface AuthPresenter {

    /**
     * Validates the credentials sent by a user and calls, if found, required callback methods.
     *
     * @param url                         the user's provider url
     * @param sharedPreferencesRepository the repository of SharedPreferences.
     */
    fun validateInfo(url: String, sharedPreferencesRepository: SharedPreferencesRepository)

}