package com.zaclimon.tsutaeru.ui.auth

import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v4.app.FragmentActivity
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.util.ActivityUtil

/**
 * Activity class used to start an authentication (sign-in) process for
 * a Tsutaeru user.
 *
 * @author zaclimon
 */
class AuthActivityTv : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityUtil.isTvMode(this)) {
            setTheme(R.style.TvTheme)
            GuidedStepSupportFragment.addAsRoot(this, WelcomeGuidedFragment(), android.R.id.content)
        }
    }

    override fun onBackPressed() {
        // Similar to the Leanback showcase, exit the Activity if we're connected.
        val guidedStepSupportFragment = GuidedStepSupportFragment.getCurrentGuidedStepSupportFragment(supportFragmentManager)

        if (guidedStepSupportFragment != null && guidedStepSupportFragment is UserConnectedGuidedFragment) {
            setResult(FragmentActivity.RESULT_OK)
            finish()
        } else {
            if (guidedStepSupportFragment != null && guidedStepSupportFragment is WelcomeGuidedFragment) {
                setResult(FragmentActivity.RESULT_CANCELED)
            }
            super.onBackPressed()
        }
    }

}