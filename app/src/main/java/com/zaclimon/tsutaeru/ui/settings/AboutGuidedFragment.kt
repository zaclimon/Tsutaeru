package com.zaclimon.tsutaeru.ui.settings

import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import com.zaclimon.tsutaeru.BuildConfig
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.ui.settings.userinfo.UserInfoGuidedFragment

/**
 * Fragment that shows the about section of the app the user information.
 *
 * @author zaclimon
 */
class AboutGuidedFragment : GuidedStepSupportFragment() {

    companion object {
        private const val ACTION_USER_INFO: Long = 0
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.app_name)
        val description = getString(R.string.version_text, BuildConfig.VERSION_NAME)

        return GuidanceStylist.Guidance(title, description, null, null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val userInfoAction = GuidedAction.Builder(context)
        val okAction = GuidedAction.Builder(context)

        userInfoAction.title(R.string.user_info_text)
        userInfoAction.id(ACTION_USER_INFO)
        okAction.clickAction(GuidedAction.ACTION_ID_OK)

        actions.add(userInfoAction.build())
        actions.add(okAction.build())
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        when (action?.id) {
            ACTION_USER_INFO -> add(fragmentManager, UserInfoGuidedFragment())
            else -> activity?.finish()
        }
    }
}