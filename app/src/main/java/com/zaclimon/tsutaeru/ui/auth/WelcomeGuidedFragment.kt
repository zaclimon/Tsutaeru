package com.zaclimon.tsutaeru.ui.auth

import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import com.zaclimon.tsutaeru.R

class WelcomeGuidedFragment : GuidedStepSupportFragment() {

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.welcome_text)
        val description = getString(R.string.authentication_required)

        return GuidanceStylist.Guidance(title, description, null, null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val yesAction = GuidedAction.Builder(context)
        val noAction = GuidedAction.Builder(context)
        yesAction.title(R.string.lets_go_text)
        noAction.title(R.string.not_yet_text)
        yesAction.id(GuidedAction.ACTION_ID_YES)
        noAction.id(GuidedAction.ACTION_ID_NO)
        actions.add(yesAction.build())
        actions.add(noAction.build())
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        val id = action?.id

        if (id == GuidedAction.ACTION_ID_YES) {
            // Switch to url Fragment
            add(fragmentManager, ProviderSetupGuidedFragment())
        } else {
            activity?.finish()
        }
    }

}