package com.zaclimon.aceiptv.auth;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.main.MainActivity;
import com.zaclimon.aceiptv.util.AceChannelUtil;
import com.zaclimon.aceiptv.util.Constants;

/**
 * Created by isaac on 17-06-07.
 */

public class AuthActivityTv extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.ACE_IPTV_PREFERENCES, MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");

        ComponentName componentName = getCallingActivity();

        if (componentName != null && !username.isEmpty() && !password.isEmpty()) {
            setResult(RESULT_OK);
            finish();
        } else if (!username.isEmpty() && !password.isEmpty()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_auth_tv);
    }
}
