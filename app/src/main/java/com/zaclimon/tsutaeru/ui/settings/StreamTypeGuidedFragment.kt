package com.zaclimon.tsutaeru.ui.settings

import android.content.ComponentName
import android.content.Context
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
 * Setting option that lets the user choose between either the MPEG-TS or the HLS
 * streaming format.
 *
 * @author zaclimon
 */
class StreamTypeGuidedFragment : GuidedStepSupportFragment() {

    companion object {
        private const val ACTION_HLS: Long = 0
        private const val ACTION_MPEG_TS: Long = 1
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val sharedPreferences = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val streamType = sharedPreferences?.getString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS)
        val title = getString(R.string.stream_type)
        val description = getString(R.string.stream_type_description)
        val breadcrumb = if (streamType == Constants.STREAM_TYPE_MPEG_TS) {
            getString(R.string.current_status_text, getString(R.string.mpeg_ts_text))
        } else {
            getString(R.string.current_status_text, getString(R.string.hls_text))
        }

        return GuidanceStylist.Guidance(title, description, breadcrumb, null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val hlsAction = GuidedAction.Builder(context).apply {
            title(R.string.hls_text)
            id(ACTION_HLS)
        }.build()

        val tsAction = GuidedAction.Builder(context).apply {
            title(R.string.mpeg_ts_text)
            id(ACTION_MPEG_TS)
        }.build()

        actions.add(hlsAction)
        actions.add(tsAction)
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        val sharedPreferences = context?.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val initialAction = sharedPreferences?.getString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS)
        val id = action?.id

        when(id) {
            ACTION_HLS -> editor?.putString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS)
            ACTION_MPEG_TS -> editor?.putString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_MPEG_TS)
            else -> editor?.putString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS)
        }

        editor?.apply()

        val modifiedAction = sharedPreferences?.getString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS)

        if (initialAction != modifiedAction) {
            // Sync the channels to reflect the latest changes only if the stream type is different from before.
            val inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT)
            EpgSyncJobService.requestImmediateSync(context, inputId, ComponentName(context, TsutaeruJobService::class.java))
            add(fragmentManager, EpgSyncLoadingGuidedFragment())
        } else {
            activity?.finish()
        }
    }

}