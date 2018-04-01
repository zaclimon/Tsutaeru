package com.zaclimon.tsutaeru.ui.auth

import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.widget.TextView
import android.widget.Toast
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.repository.SharedPreferencesRepositoryImpl
import com.zaclimon.tsutaeru.util.Constants

class VerificationGuidedFragment : GuidedStepSupportFragment(), AuthView {

    override fun onStart() {
        super.onStart()

        val url = arguments?.getString(UrlInputGuidedFragment.ARGUMENT_URL)
        val username = arguments?.getString(UsernameInputGuidedFragment.ARGUMENT_USERNAME)
        val password = arguments?.getString(PasswordInputGuidedFragment.ARGUMENT_PASSWORD)
        val loadingTextView = activity?.findViewById<TextView>(R.id.loading_title)
        val authPresenter = AuthPresenterImpl(this)
        val serviceUrl = getString(R.string.provider_playlist_url, url, username, password, Constants.STREAM_TYPE_HLS)

        loadingTextView?.text = getString(R.string.wont_be_long_text)
        authPresenter.validateInfo(serviceUrl, SharedPreferencesRepositoryImpl(context))
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
        popBackStackToGuidedStepSupportFragment(UrlInputGuidedFragment::class.java, 0)
    }

    override fun onConnectionSuccess() {
        val fragment = UserConnectedGuidedFragment()
        val fragmentArguments = Bundle()
        fragmentArguments.putString(UsernameInputGuidedFragment.ARGUMENT_USERNAME, arguments?.getString(UsernameInputGuidedFragment.ARGUMENT_USERNAME))
        fragment.arguments = fragmentArguments
        add(fragmentManager, fragment)
    }

    override fun onTimeoutReceived() {
        Toast.makeText(context, R.string.connection_timeout_toast, Toast.LENGTH_SHORT).show()
        popBackStackToGuidedStepSupportFragment(UrlInputGuidedFragment::class.java, 0)
    }
}