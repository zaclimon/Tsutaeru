package com.zaclimon.tsutaeru.ui.auth;

import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v4.app.FragmentActivity;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.util.ActivityUtil;

/**
 * Activity class used to start an authentication (sign-in) process for
 * a Tsutaeru user.
 *
 * @author zaclimon
 * Creation date: 07/06/17
 */

public class AuthActivityTv extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityUtil.isTvMode(this)) {
            setTheme(R.style.TvTheme);
            GuidedStepSupportFragment.addAsRoot(this, new FirstStepAuthGuidedFragment(), android.R.id.content);
        }
    }

    @Override
    public void onBackPressed() {
        // Similar to the Leanback showcase, exit the Activity if we're connected.
        GuidedStepSupportFragment guidedStepFragment = GuidedStepSupportFragment.getCurrentGuidedStepSupportFragment(getSupportFragmentManager());

        if (guidedStepFragment != null && guidedStepFragment instanceof FifthStepAuthGuidedFragment) {
            finish();
        } else {
            super.onBackPressed();
        }

    }

}
