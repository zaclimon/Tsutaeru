package com.zaclimon.tsutaeru.ui.settings

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.media.tv.TvContract
import android.os.Bundle
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import com.google.android.media.tv.companionlibrary.sync.EpgSyncJobService
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.service.TsutaeruJobService
import com.zaclimon.tsutaeru.util.Constants

/**
 * [GuidedStepSupportFragment] responsible for filtering channels from a given provider based on the
 * their playlist group.
 *
 * @author zaclimon
 */
class ChannelGroupGuidedFragment : GuidedStepSupportFragment() {

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.channel_group_title)
        val description = getString(R.string.channel_group_description)

        return GuidanceStylist.Guidance(title, description, null, null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val sharedPreferences = requireContext().getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val entriesMap = sharedPreferences.all
        val preferencesGroupsSet = sortedSetOf<String>()
        val contentProviderGroupsSet = generateGroupsSet()

          /*
          If a channel's group has been disabled, the channel is deleted entirely from the system's
          ContentProvider. Retrieve the groups from the SharedPreferences in that case.
          */

        for (key in entriesMap.keys) {
            if (key.contains(Constants.CHANNEL_GROUP_PREFERENCE)) {
                preferencesGroupsSet.add(key)
            }
        }

        if (preferencesGroupsSet.isNotEmpty()) {
            // Add new groups that might not have been synced to the preferences set.
            preferencesGroupsSet.addAll(contentProviderGroupsSet)

            for (group in preferencesGroupsSet) {
                val title = group.removePrefix(Constants.CHANNEL_GROUP_PREFERENCE)
                actions.add(createGroupAction(title, sharedPreferences))
            }
        } else if (contentProviderGroupsSet.isNotEmpty()) {
            for (group in contentProviderGroupsSet) {
                actions.add(createGroupAction(group, sharedPreferences))
            }
        }

        if (actions.isEmpty()) {
            val emptyAction = GuidedAction.Builder(context).apply {
                title(R.string.no_channel_detected)
                id(GuidedAction.ACTION_ID_CANCEL)
            }.build()
            actions.add(emptyAction)
        } else {
            val okAction = GuidedAction.Builder(context).apply {
                clickAction(GuidedAction.ACTION_ID_OK)
            }.build()

            val cancelAction = GuidedAction.Builder(context).apply {
                clickAction(GuidedAction.ACTION_ID_CANCEL)
            }.build()

            actions.add(okAction)
            actions.add(cancelAction)
        }
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        val id = action?.id

        if (id == GuidedAction.ACTION_ID_OK) {
            val sharedPreferences = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            var hasAnyPreferenceChanged = false

            // Size of all the actions without "ok" and "cancel"
            val checkboxActionsSize = actions.size - 2
            for (i in 0 until checkboxActionsSize) {
                val currentPreference = sharedPreferences?.getBoolean(Constants.CHANNEL_GROUP_PREFERENCE + actions[i].title.toString(), true)
                if (currentPreference != actions[i].isChecked) {
                    hasAnyPreferenceChanged = true
                }
                editor?.putBoolean(Constants.CHANNEL_GROUP_PREFERENCE + actions[i].title, actions[i].isChecked)
            }

            if (hasAnyPreferenceChanged) {
                val inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT)
                EpgSyncJobService.requestImmediateSync(context, inputId, ComponentName(context, TsutaeruJobService::class.java))
                editor?.apply()
                add(fragmentManager, EpgSyncLoadingGuidedFragment())
            } else {
                activity?.finish()
            }

        } else if (id == GuidedAction.ACTION_ID_CANCEL) {
            activity?.finish()
        }
    }

    private fun generateGroupsSet(): Set<String> {
        val networkAffiliationColumn = TvContract.Channels.COLUMN_NETWORK_AFFILIATION
        val contentResolver = context?.contentResolver
        val inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT)
        val uri = TvContract.buildChannelsUriForInput(inputId)
        val set = sortedSetOf<String>()

        val cursor = contentResolver?.query(uri, arrayOf(networkAffiliationColumn), null, null, null)
        cursor?.let {
            val index = it.getColumnIndex(networkAffiliationColumn)
            while (it.moveToNext()) {
                set.add(it.getString(index))
            }
        }
        cursor?.close()
        return set
    }

    private fun createGroupAction(title: String, sharedPreferences: SharedPreferences): GuidedAction {
        return GuidedAction.Builder(context).apply {
            title(title)
            checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID)
            checked(sharedPreferences.getBoolean(Constants.CHANNEL_GROUP_PREFERENCE + title, true))
        }.build()
    }
}