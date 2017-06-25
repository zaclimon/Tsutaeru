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
import com.zaclimon.aceiptv.util.Utilities;

import static android.R.attr.password;
import static android.support.v17.leanback.app.GuidedStepFragment.getCurrentGuidedStepFragment;

/**
 * Created by isaac on 17-06-07.
 */

public class AuthActivityTv extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Utilities.isTvMode(this)) {
            GuidedStepFragment.addAsRoot(this, new WelcomeStepAuthGuidedFragment(), android.R.id.content);
        }
    }

    @Override
    public void onBackPressed() {
        // Similar to the Leanback showcase, exit the Activity if we're connected.
        GuidedStepFragment guidedStepFragment = GuidedStepFragment.getCurrentGuidedStepFragment(getFragmentManager());

        if (guidedStepFragment != null && guidedStepFragment instanceof ConnectedStepAuthGuidedFragment) {
            finish();
        } else {
            super.onBackPressed();
        }

    }

}
