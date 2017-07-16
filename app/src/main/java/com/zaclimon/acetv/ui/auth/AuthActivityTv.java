package com.zaclimon.acetv.ui.auth;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.zaclimon.acetv.R;
import com.zaclimon.acetv.util.ActivityUtil;

/**
 * Activity class used to start an authentication (sign-in) process for
 * an Ace TV user.
 *
 * @author zaclimon
 * Creation date: 07/06/17
 */

public class AuthActivityTv extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityUtil.isTvMode(this)) {
            setTheme(R.style.TvTheme);
            GuidedStepFragment.addAsRoot(this, new FirstStepAuthGuidedFragment(), android.R.id.content);
        }
    }

    @Override
    public void onBackPressed() {
        // Similar to the Leanback showcase, exit the Activity if we're connected.
        GuidedStepFragment guidedStepFragment = GuidedStepFragment.getCurrentGuidedStepFragment(getFragmentManager());

        if (guidedStepFragment != null && guidedStepFragment instanceof FifthStepAuthGuidedFragment) {
            finish();
        } else {
            super.onBackPressed();
        }

    }

}
