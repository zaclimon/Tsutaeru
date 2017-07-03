package com.zaclimon.aceiptv.ui.auth;

import com.zaclimon.aceiptv.repository.SharedPreferencesRepository;

/**
 * Interface that defines a valid Presenter used for the authentication process.
 *
 * @author zaclimon
 * Creation date: 07/06/17
 */

public interface AuthPresenter {

    /**
     * Validates the credentials sent by a user and calls, if found, required callback methods.
     * @param username the user's username
     * @param password the user's password
     * @param sharedPreferencesRepository the repository of SharedPreferences.
     */
    void validateInfo(String username, String password, SharedPreferencesRepository sharedPreferencesRepository);
}
