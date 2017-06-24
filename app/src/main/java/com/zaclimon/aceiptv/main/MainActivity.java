package com.zaclimon.aceiptv.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.UiModeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.auth.AuthActivityTv;
import com.zaclimon.aceiptv.util.Constants;
import com.zaclimon.aceiptv.util.Utilities;


/**
 * Created by isaac on 17-06-20.
 */

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (isTvMode() && Utilities.isUsernamePasswordEmpty(this)) {
            Intent intent = new Intent(this, AuthActivityTv.class);
            startActivity(intent);
            finish();
        } else if (isTvMode()) {
            fragmentTransaction.add(R.id.activity_fragment_holder, new MainTvFragment());
        }

        fragmentTransaction.commit();
    }

    private boolean isTvMode() {
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        return (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION);
    }

}
