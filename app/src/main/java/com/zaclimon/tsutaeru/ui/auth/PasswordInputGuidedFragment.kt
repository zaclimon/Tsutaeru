package com.zaclimon.tsutaeru.ui.auth

import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import android.support.v17.leanback.widget.GuidedActionsStylist
import android.text.InputType
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.ui.components.guidedactionstylist.TsutaeruGuidedActionStylist

/**
 * [GuidedStepSupportFragment] asking for the user it's provider password.
 *
 * @author zaclimon
 */
class PasswordInputGuidedFragment : GuidedStepSupportFragment() {

    companion object {
        internal const val ACTION_PASSWORD : Long = 0
        internal const val ARGUMENT_PASSWORD = "password"
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.enter_password_title)
        return (GuidanceStylist.Guidance(title, null, null, null))
    }

    override fun onCreateActionsStylist(): GuidedActionsStylist {
        return TsutaeruGuidedActionStylist()
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val passwordAction = GuidedAction.Builder(context)
        passwordAction.title(R.string.password_text)
        passwordAction.editTitle("")
        passwordAction.editable(true)
        passwordAction.editInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        passwordAction.id(ACTION_PASSWORD)
        actions.add(passwordAction.build())
    }

    override fun onGuidedActionEditedAndProceed(action: GuidedAction?): Long {
        val actionId = action?.id

        if (actionId == UsernameInputGuidedFragment.ACTION_USERNAME) {
            val password = action.editTitle.toString()
            if (password.isNotEmpty()) {
                val fragment = VerificationGuidedFragment()
                val currentArguments = Bundle()
                currentArguments.putString(UrlInputGuidedFragment.ARGUMENT_URL, arguments?.getString(UrlInputGuidedFragment.ARGUMENT_URL))
                currentArguments.putString(UsernameInputGuidedFragment.ARGUMENT_USERNAME, arguments?.getString(UsernameInputGuidedFragment.ARGUMENT_USERNAME))
                currentArguments.putString(ARGUMENT_PASSWORD, password)
                fragment.arguments = currentArguments
                add(fragmentManager, fragment)
            }
        }
        return (GuidedAction.ACTION_ID_CURRENT)
    }

}