package com.zaclimon.tsutaeru.ui.auth

import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import android.text.InputType
import com.zaclimon.tsutaeru.R

/**
 * [GuidedStepSupportFragment] asking for the user it's provider username.
 *
 * @author zaclimon
 */
class UsernameInputGuidedFragment : GuidedStepSupportFragment() {

    companion object {
        internal const val ACTION_USERNAME : Long = 0
        internal const val ARGUMENT_USERNAME = "username"
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.enter_username_title)
        return (GuidanceStylist.Guidance(title, null, null, null))
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val usernameAction = GuidedAction.Builder(context)
        usernameAction.title(R.string.username_text)
        usernameAction.editTitle("")
        usernameAction.editable(true)
        usernameAction.inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        usernameAction.id(ACTION_USERNAME)
        actions.add(usernameAction.build())
    }

    override fun onGuidedActionEditedAndProceed(action: GuidedAction?): Long {
        val actionId = action?.id

        if (actionId == ACTION_USERNAME) {
            val username = action.editTitle.toString()
            if (username.isNotEmpty()) {
                val fragment = PasswordInputGuidedFragment()
                val currentArguments = Bundle()
                currentArguments.putString(UrlInputGuidedFragment.ARGUMENT_URL, arguments?.getString(UrlInputGuidedFragment.ARGUMENT_URL))
                currentArguments.putString(ARGUMENT_USERNAME, username)
                fragment.arguments = currentArguments
                add(fragmentManager, fragment)
            }
        }
        return (GuidedAction.ACTION_ID_CURRENT)
    }

}