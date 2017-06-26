package com.zaclimon.aceiptv.auth;

import android.os.AsyncTask;

import com.zaclimon.aceiptv.util.SharedPreferencesRepository;
import com.zaclimon.aceiptv.util.Constants;
import com.zaclimon.aceiptv.util.RichFeedUtil;

import java.io.IOException;


/**
 * AsyncTask class that verifies the given credentials of a given user.
 *
 * Note that for it to be used, a {@link AuthView} and a {@link SharedPreferencesRepository} are
 * required since callback to both of them will be made.
 *
 * @author zaclimon
 * Creation date: 25/06/17
 */

public class AsyncAuthValidateInfo extends AsyncTask<Void, Void, Boolean> {

    private String asyncUsername;
    private String asyncPassword;
    private AuthView mAuthView;
    private SharedPreferencesRepository mSharedPreferencesRepository;

    /**
     * Default constructor of the class
     * @param username the username of the user
     * @param password the password of the user
     * @param authView the AuthView in which callbacks will be called
     * @param sharedPreferencesRepository the SharedPreferencesRepository used to save the credentials
     */
    public AsyncAuthValidateInfo(String username, String password, AuthView authView, SharedPreferencesRepository sharedPreferencesRepository) {
        asyncUsername = username;
        asyncPassword = password;
        mAuthView = authView;
        mSharedPreferencesRepository = sharedPreferencesRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String m3uLink = mAuthView.getPlaylistLink(asyncUsername, asyncPassword);
            RichFeedUtil.getInputStream(m3uLink);
            return (true);
        } catch (IOException io) {
            return (false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            mSharedPreferencesRepository.putString(Constants.USERNAME_PREFERENCE, asyncUsername);
            mSharedPreferencesRepository.putString(Constants.PASSWORD_PREFERENCE, asyncPassword);
            mSharedPreferencesRepository.apply();
            mAuthView.onConnectionSuccess();
        } else {
            mAuthView.onWrongCredentialsReceived();
        }
    }

}
