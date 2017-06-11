package com.zaclimon.aceiptv.auth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.util.AceChannelUtil;

/**
 * Created by isaac on 17-06-07.
 */

public class AuthActivityTv extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(AceChannelUtil.ACE_IPTV_PREFERENCES, MODE_PRIVATE);
        String username = sharedPreferences.getString(AceChannelUtil.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(AceChannelUtil.PASSWORD_PREFERENCE, "");

        if (!username.isEmpty() && !password.isEmpty()) {
            finish();
        }

        setContentView(R.layout.activity_auth_tv);
    }
}
