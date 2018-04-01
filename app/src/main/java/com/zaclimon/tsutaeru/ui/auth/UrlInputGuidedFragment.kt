package com.zaclimon.tsutaeru.ui.auth

import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import android.text.InputType
import com.zaclimon.tsutaeru.R

/**
 * [GuidedStepSupportFragment] asking for the user it's provider playlist URL.
 *
 * @author zaclimon
 */
class UrlInputGuidedFragment : GuidedStepSupportFragment() {

    companion object {
        internal const val ACTION_URL_PROVIDER : Long = 0
        internal const val ARGUMENT_URL = "url"
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.enter_provider_url_title)
        val description = getString(R.string.enter_provider_url_description)

        return (GuidanceStylist.Guidance(title, description, null, null))
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val urlAction = GuidedAction.Builder(context)
        urlAction.title(R.string.provider_url_text)
        urlAction.editTitle("")
        urlAction.editable(true)
        urlAction.inputType(InputType.TYPE_CLASS_TEXT)
        urlAction.id(ACTION_URL_PROVIDER)
        actions.add(urlAction.build())
    }

    override fun onGuidedActionEditedAndProceed(action: GuidedAction?): Long {
        val actionId = action?.id

        if (actionId == ACTION_URL_PROVIDER) {
            val url = action.editTitle.toString()
            if (url.isNotEmpty()) {
                val fragment = UsernameInputGuidedFragment()
                val arguments = Bundle()
                arguments.putString(ARGUMENT_URL, url)
                fragment.arguments = arguments
                add(fragmentManager, fragment)
            }
        }
        return (GuidedAction.ACTION_ID_CURRENT)
    }
}