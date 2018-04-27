package com.zaclimon.tsutaeru.ui.auth

import android.content.Context
import android.media.tv.TvContract
import android.os.AsyncTask
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.widget.TextView
import android.widget.Toast
import com.google.android.media.tv.companionlibrary.model.ModelUtils
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.data.TsutaeruDatabase
import com.zaclimon.tsutaeru.repository.SharedPreferencesRepositoryImpl
import com.zaclimon.tsutaeru.util.Constants
import java.lang.ref.WeakReference

/**
 * [GuidedStepSupportFragment] verifying whether the user can use it's provider services.
 *
 * @author zaclimon
 */
class VerificationGuidedFragment : GuidedStepSupportFragment(), AuthView {

    override fun onStart() {
        val fragmentContext = context

        fragmentContext?.let {
            super.onStart()

            val url = arguments?.getString(UrlInputGuidedFragment.ARGUMENT_URL)
            val username = arguments?.getString(UsernameInputGuidedFragment.ARGUMENT_USERNAME)?.trim()
            val password = arguments?.getString(PasswordInputGuidedFragment.ARGUMENT_PASSWORD)?.trim()
            val loadingTextView = activity?.findViewById<TextView>(R.id.loading_title)
            val authPresenter = AuthPresenterImpl(this)
            val serviceUrl = getString(R.string.provider_playlist_url, url, username, password, Constants.STREAM_TYPE_HLS)

            loadingTextView?.text = getString(R.string.wont_be_long_text)
            authPresenter.validateInfo(serviceUrl, SharedPreferencesRepositoryImpl(fragmentContext))
        }
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

    override fun onConnectionSuccess(isAccountChanged: Boolean) {

        if (isAccountChanged) {
            activity?.callingActivity?.let {
                // Since the calling activity may be the settings, let's erase everything.
                AsyncDeleteChannels(context!!).execute()
                val contentDao = TsutaeruDatabase.getInstance(context!!).avContentDao()
                contentDao.deleteAll()
            }
        }

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

    private class AsyncDeleteChannels(context: Context) : AsyncTask<Void, Void, Void?>() {

        private val asyncReference = WeakReference(context)

        override fun doInBackground(vararg params: Void?): Void? {
            val context = asyncReference.get()

            context?.let {
                val contentResolver = it.contentResolver
                val channels = ModelUtils.getChannels(contentResolver)
                for (channel in channels) {
                    val channelUri = TvContract.buildChannelUri(channel.id)
                    contentResolver.delete(channelUri, null, null)
                }
            }
            return null
        }
    }
}