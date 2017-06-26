package com.zaclimon.aceiptv.auth;

import android.os.AsyncTask;

import com.zaclimon.aceiptv.util.SharedPreferencesRepository;
import com.zaclimon.aceiptv.util.Constants;
import com.zaclimon.aceiptv.util.RichFeedUtil;

import java.io.IOException;

/**
 * Created by isaac on 17-06-25.
 */

public class AsyncAuthValidateInfo extends AsyncTask<Void, Void, Boolean> {

    private String asyncUsername;
    private String asyncPassword;
    private AuthView mAuthView;
    private SharedPreferencesRepository mSharedPreferencesRepository;

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
            RichFeedUtil.getInputStream(m3uLink);
            return (true);
        } catch (IOException io) {
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
        } else {
            mAuthView.onWrongCredentialsReceived();
        }
    }

}
