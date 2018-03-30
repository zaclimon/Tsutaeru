package com.zaclimon.tsutaeru.ui.auth;

import android.os.AsyncTask;
import android.util.Log;

import com.zaclimon.tsutaeru.repository.SharedPreferencesRepository;
import com.zaclimon.tsutaeru.util.Constants;
import com.zaclimon.tsutaeru.util.NetworkUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;


/**
 * AsyncTask class that verifies the given credentials of a given user.
 * <p>
 * Note that for it to be used, a {@link AuthView} and a {@link SharedPreferencesRepository} are
 * required since callback to both of them will be made.
 *
 * @author zaclimon
 * Creation date: 25/06/17
 */

public class AsyncAuthValidateInfo extends AsyncTask<Void, Void, Boolean> {

    private String asyncUsername;
    private String asyncPassword;
    private IOException mIOException;
    private AuthView mAuthView;
    private SharedPreferencesRepository mSharedPreferencesRepository;

    /**
     * Default constructor of the class
     *
     * @param username                    the username of the user
     * @param password                    the password of the user
     * @param authView                    the AuthView in which callbacks will be called
     * @param sharedPreferencesRepository the SharedPreferencesRepository used to save the credentials
     */
    public AsyncAuthValidateInfo(String username, String password, AuthView authView, SharedPreferencesRepository sharedPreferencesRepository) {
        asyncUsername = username;
        asyncPassword = password;
        mAuthView = authView;
        mSharedPreferencesRepository = sharedPreferencesRepository;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String m3uLink = mAuthView.getPlaylistLink(asyncUsername, asyncPassword);
            NetworkUtils.getNetworkInputStream(m3uLink);
            return (true);
        } catch (IOException io) {
            Log.d(getClass().getSimpleName(), io.toString());
            mIOException = io;
            return (false);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            mSharedPreferencesRepository.putString(Constants.USERNAME_PREFERENCE, asyncUsername);
            mSharedPreferencesRepository.putString(Constants.PASSWORD_PREFERENCE, asyncPassword);
            mSharedPreferencesRepository.apply();
            mAuthView.onConnectionSuccess();
        } else if (mIOException instanceof FileNotFoundException) {
            mAuthView.onWrongCredentialsReceived();
        } else if (mIOException instanceof SocketTimeoutException) {
            mAuthView.onTimeoutReceived();
        }
    }

}
