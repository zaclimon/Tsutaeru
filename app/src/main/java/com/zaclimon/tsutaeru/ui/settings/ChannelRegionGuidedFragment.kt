package com.zaclimon.tsutaeru.ui.settings

import android.content.ComponentName
import android.content.Context
import android.media.tv.TvContract
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import com.google.android.media.tv.companionlibrary.EpgSyncJobService
import com.zaclimon.tsutaeru.BuildConfig
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.service.TsutaeruJobService
import com.zaclimon.tsutaeru.ui.settings.userinfo.UserInfoGuidedFragment
import com.zaclimon.tsutaeru.util.Constants
import java.util.concurrent.TimeUnit

/**
 * [GuidedStepSupportFragment] which lists several regions in which the user can select for his/her
 * Live TV experience.
 *
 * @author zaclimon
 */
class ChannelRegionGuidedFragment : GuidedStepSupportFragment() {

    companion object {
        private const val ACTION_UK_REGION_ID: Long = 0
        private const val ACTION_NA_REGION_ID: Long = 1
        private const val ACTION_INT_REGION_ID: Long = 2
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.channel_region_title)
        val description = getString(R.string.channel_region_description)

        return GuidanceStylist.Guidance(title, description, null, null)
    }


    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val sharedPreferences = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val ukAction = GuidedAction.Builder(context)
        val naAction = GuidedAction.Builder(context)
        val intAction = GuidedAction.Builder(context)
        val okAction = GuidedAction.Builder(context)

        // Set the titles
        ukAction.title(R.string.region_uk)
        naAction.title(R.string.region_na)
        intAction.title(R.string.region_international)

        // Set the id's
        ukAction.id(ACTION_UK_REGION_ID)
        naAction.id(ACTION_NA_REGION_ID)
        intAction.id(ACTION_INT_REGION_ID)

        // Set the actions to have checkboxes
        ukAction.checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID)
        naAction.checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID)
        intAction.checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID)

        // Set if the value is checked, all of them are enabled by default
        val isUkChecked = sharedPreferences?.getBoolean(Constants.UK_REGION_PREFERENCE, true) ?: true
        val isNaChecked = sharedPreferences?.getBoolean(Constants.NA_REGION_PREFERENCE, true) ?: true
        val isIntChecked = sharedPreferences?.getBoolean(Constants.INTERNATIONAL_REGION_PREFERENCE, true) ?: true
        ukAction.checked(isUkChecked)
        naAction.checked(isNaChecked)
        intAction.checked(isIntChecked)

        okAction.clickAction(GuidedAction.ACTION_ID_OK)

        actions.add(ukAction.build())
        actions.add(naAction.build())
        actions.add(intAction.build())
        actions.add(okAction.build())
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        val id = action?.id

        if (id == GuidedAction.ACTION_ID_OK) {
            val sharedPreferences = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()

            val isUkEnabled = sharedPreferences?.getBoolean(Constants.UK_REGION_PREFERENCE, true)
            val isNaEnabled = sharedPreferences?.getBoolean(Constants.NA_REGION_PREFERENCE, true)
            val isIntEnabled = sharedPreferences?.getBoolean(Constants.INTERNATIONAL_REGION_PREFERENCE, true)

            /*
             A statically defined check shouldn't be the thing to do but since we already know the
             number of elements, it should not be that bad.
             */

            if (actions[0].isChecked != isUkEnabled || actions[1].isChecked != isNaEnabled || actions[2].isChecked != isIntEnabled) {
                val inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT)
                EpgSyncJobService.requestImmediateSync(context, inputId, TimeUnit.HOURS.toMillis(48), ComponentName(context, TsutaeruJobService::class.java))
                editor?.putBoolean(Constants.UK_REGION_PREFERENCE, actions[0].isChecked)
                editor?.putBoolean(Constants.NA_REGION_PREFERENCE, actions[1].isChecked)
                editor?.putBoolean(Constants.INTERNATIONAL_REGION_PREFERENCE, actions[2].isChecked)
                editor?.apply()
                add(fragmentManager, EpgSyncLoadingGuidedFragment())
            }
        }
    }

}