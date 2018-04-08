package com.zaclimon.tsutaeru.ui.settings

import android.content.Context
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import android.text.InputType
import android.widget.Toast
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.util.Constants

/**
 * Settings section used for setting up the EPG offset for TV Catchup content
 *
 * @author zaclimon
 */
class TvCatchupEpgOffsetGuidedFragment : GuidedStepSupportFragment() {

    companion object {
        private const val ACTION_OFFSET: Long = 0
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val sharedPreferences = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val epgOffset = sharedPreferences?.getInt(Constants.EPG_OFFSET_PREFERENCE, 0)

        val title = getString(R.string.epg_offset_title)
        val description = getString(R.string.epg_offset_description)
        val breadcrumb = getString(R.string.current_status_text, getString(R.string.hours_text, epgOffset))

        return GuidanceStylist.Guidance(title, description, breadcrumb, null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val offsetAction = GuidedAction.Builder(context)
        offsetAction.title(R.string.offset_text)
        offsetAction.editTitle("")
        offsetAction.editable(true)
        offsetAction.editInputType(InputType.TYPE_CLASS_NUMBER and InputType.TYPE_NUMBER_FLAG_DECIMAL and InputType.TYPE_NUMBER_FLAG_SIGNED)
        offsetAction.id(ACTION_OFFSET)
        actions.add(offsetAction.build())
    }

    override fun onGuidedActionEditedAndProceed(action: GuidedAction?): Long {
        val id = action?.id

        if (id == ACTION_OFFSET) {
            val offset = action.editTitle.toString()

            if (offset.isNotEmpty() && offset.isNotBlank()) {
                Toast.makeText(context, R.string.clear_cache_toast, Toast.LENGTH_SHORT).show()
                val editor = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)?.edit()
                editor?.putInt(Constants.EPG_OFFSET_PREFERENCE, offset.toInt())
                editor?.apply()
                activity?.finish()
            } else {
                action.title = getString(R.string.offset_empty)
            }
        }
        return (GuidedAction.ACTION_ID_CURRENT)
    }
}