package com.zaclimon.tsutaeru.ui.auth

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.widget.TextView
import android.widget.Toast
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.repository.SharedPreferencesRepositoryImpl

class VerificationGuidedFragment : GuidedStepSupportFragment(), AuthView {

    override fun onStart() {
        super.onStart()

        val url = arguments?.getString(ProviderSetupGuidedFragment.URL_ARGUMENT)
        val loadingTextView = activity?.findViewById<TextView>(R.id.loading_title)
        val authPresenter = AuthPresenterImpl(this)

        loadingTextView?.text = getString(R.string.wont_be_long_text)
        authPresenter.validateInfo(url, SharedPreferencesRepositoryImpl(context))
    }

    override fun onCreateGuidanceStylist(): GuidanceStylist {
        return (object : GuidanceStylist() {
            override fun onProvideLayoutId(): Int {
                return (R.layout.fragment_guided_wizard_loading)
            }
        })
    }

    override fun onConnectionFailed() {
        Toast.makeText(context, R.string.wrong_credentials_toast, Toast.LENGTH_SHORT).show()
        popBackStackToGuidedStepSupportFragment(ProviderSetupGuidedFragment::class.java, 0)
    }

    override fun onConnectionSuccess() {
        add(fragmentManager, UserConnectedGuidedFragment())
    }

    override fun onTimeoutReceived() {
        Toast.makeText(context, R.string.connection_timeout_toast, Toast.LENGTH_SHORT).show()
        popBackStackToGuidedStepSupportFragment(ProviderSetupGuidedFragment::class.java, 0)
    }
}