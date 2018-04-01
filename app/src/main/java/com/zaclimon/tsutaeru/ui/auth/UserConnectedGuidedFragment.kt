package com.zaclimon.tsutaeru.ui.auth

import android.app.Activity
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.ui.tvinputsetup.TsutaeruTvInputSetupActivity

/**
 *  [GuidedStepSupportFragment] that confirms when the user has been able to connect to it's
 *  provider.
 *
 *  @author zaclimon
 */
class UserConnectedGuidedFragment : GuidedStepSupportFragment() {

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.connection_successful_text)
        val componentName = activity?.callingActivity
        val description = if (componentName?.className == TsutaeruTvInputSetupActivity::class.java.name) {
            getString(R.string.connected_description_live_channels)
        } else {
            getString(R.string.connected_description_standalone)
        }

        return (GuidanceStylist.Guidance(title, description, null, null))
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val exitAction = GuidedAction.Builder(context)
        exitAction.title(R.string.lets_go_text)
        exitAction.id(GuidedAction.ACTION_ID_OK)
        actions.add(exitAction.build())
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        activity?.setResult(Activity.RESULT_OK)
        finishGuidedStepSupportFragments()
    }

}