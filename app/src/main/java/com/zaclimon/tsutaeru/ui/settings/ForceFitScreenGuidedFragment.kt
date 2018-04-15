package com.zaclimon.tsutaeru.ui.settings

import android.content.Context
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.util.Constants

/**
 * [GuidedStepSupportFragment] used to force an audio-visual content to stretch in order to fit the total
 * resolution of a display.
 *
 * @author zaclimon
 */
class ForceFitScreenGuidedFragment : GuidedStepSupportFragment() {

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val sharedPreferences = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val title = getString(R.string.force_video_fit_title)
        val description = getString(R.string.force_video_fit_description)
        val isForceFit = sharedPreferences?.getBoolean(Constants.VIDEO_FIT_SCREEN_PREFERENCE, false) ?: false
        val breadcrumb = if (isForceFit) {
            getString(R.string.current_status_text, getString(R.string.activated_text))
        } else {
            getString(R.string.current_status_text, getString(R.string.deactivated_text))
        }

        return GuidanceStylist.Guidance(title, description, breadcrumb, null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val yesAction = GuidedAction.Builder(context).apply {
            title(R.string.yes_text)
            id(GuidedAction.ACTION_ID_YES)
        }.build()

        val noAction = GuidedAction.Builder(context).apply {
            title(R.string.no_text)
            id(GuidedAction.ACTION_ID_NO)
        }.build()

        actions.add(yesAction)
        actions.add(noAction)
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        val sharedPreferences = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val id = action?.id

        editor?.putBoolean(Constants.VIDEO_FIT_SCREEN_PREFERENCE, (id == GuidedAction.ACTION_ID_YES))
        editor?.apply()
        activity?.finish()
    }
}