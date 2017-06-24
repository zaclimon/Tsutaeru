package com.zaclimon.aceiptv.auth;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.zaclimon.aceiptv.main.MainActivity;
import com.zaclimon.aceiptv.util.Constants;

import static android.R.attr.password;

/**
 * Created by isaac on 17-06-07.
 */

public class AuthActivityTv extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isTvMode()) {
            GuidedStepFragment.addAsRoot(this, new WelcomeStepAuthGuidedFragment(), android.R.id.content);
        }
    }

    private boolean isTvMode() {
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        return (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION);
    }
}
