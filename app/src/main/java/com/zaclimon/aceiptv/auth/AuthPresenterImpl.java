package com.zaclimon.aceiptv.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.util.AceChannelUtil;
import com.zaclimon.aceiptv.util.Constants;
import com.zaclimon.aceiptv.util.RichFeedUtil;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by isaac on 17-06-07.
 */

public class AuthPresenterImpl implements AuthPresenter {

    private AuthView mAuthView;

    public AuthPresenterImpl(AuthView view) {
        mAuthView = view;
    }

    @Override
    public void validateInfo(String username, String password, Context context) {
        AsyncValidateInfos asyncValidateInfos = new AsyncValidateInfos(username, password, context);
        asyncValidateInfos.execute();
    }

    private class AsyncValidateInfos extends AsyncTask<Void, Void, Boolean> {

        private String asyncUsername;
        private String asyncPassword;
        private Context asyncContext;

        public AsyncValidateInfos(String username, String password, Context context) {
            asyncUsername = username;
            asyncPassword = password;
            asyncContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String m3uLink = asyncContext.getString(R.string.ace_playlist_url, asyncUsername, asyncPassword);
                RichFeedUtil.getInputStream(asyncContext, Uri.parse(m3uLink));
                return (true);
            } catch (IOException io) {
                return (false);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                SharedPreferences.Editor editor = asyncContext.getSharedPreferences(Constants.ACE_IPTV_PREFERENCES, Context.MODE_PRIVATE).edit();
                editor.putString(Constants.USERNAME_PREFERENCE, asyncUsername);
                editor.putString(Constants.PASSWORD_PREFERENCE, asyncPassword);
                editor.apply();
                mAuthView.onConnectionSuccess();
            } else {
                mAuthView.onConnectionFailed(asyncContext.getString(R.string.wrong_credentials));
            }
        }
    }

}
