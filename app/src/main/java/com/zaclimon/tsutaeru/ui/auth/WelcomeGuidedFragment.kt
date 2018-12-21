package com.zaclimon.tsutaeru.ui.auth

import android.os.Bundle
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import com.zaclimon.tsutaeru.R

/**
 * [GuidedStepSupportFragment] that is an initial setup screen for the user to be able to get access
 * to it's IPTV provider services.
 *
 * @author zaclimon
 */
class WelcomeGuidedFragment : GuidedStepSupportFragment() {

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.welcome_text)
        val description = getString(R.string.authentication_required)

        return GuidanceStylist.Guidance(title, description, null, null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val yesAction = GuidedAction.Builder(context).apply {
            title(R.string.lets_go_text)
            id(GuidedAction.ACTION_ID_YES)
        }.build()

        val noAction = GuidedAction.Builder(context).apply {
            title(R.string.not_yet_text)
            id(GuidedAction.ACTION_ID_NO)
        }.build()

        actions.add(yesAction)
        actions.add(noAction)
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        val id = action?.id
        when (id) {
            GuidedAction.ACTION_ID_YES -> add(fragmentManager, UrlInputGuidedFragment())
            else -> activity?.finish()
        }
    }

}