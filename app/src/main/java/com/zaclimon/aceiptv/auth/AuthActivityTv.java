package com.zaclimon.aceiptv.auth;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.zaclimon.aceiptv.util.ActivityUtil;

/**
 * Activity class used to start an authentication (sign-in) process for
 * an A.C.E. IPTV user.
 *
 * @author zaclimon
 * Creation date: 07/06/17
 *
 */

public class AuthActivityTv extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityUtil.isTvMode(this)) {
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
