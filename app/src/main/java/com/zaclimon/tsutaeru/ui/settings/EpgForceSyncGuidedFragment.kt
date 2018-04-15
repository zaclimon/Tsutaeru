package com.zaclimon.tsutaeru.ui.settings

import android.content.ComponentName
import android.media.tv.TvContract
import android.os.AsyncTask
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepSupportFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import com.google.android.media.tv.companionlibrary.model.ModelUtils
import com.google.android.media.tv.companionlibrary.sync.EpgSyncJobService
import com.google.android.media.tv.companionlibrary.utils.TvContractUtils
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.service.TsutaeruJobService
import com.zaclimon.tsutaeru.util.Constants

/**
 * Setting option that forces an EPG sync for the next hour.
 *
 * @author zaclimon
 */
class EpgForceSyncGuidedFragment : GuidedStepSupportFragment() {

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.force_epg_sync_title)
        val description = getString(R.string.force_epg_sync_description)

        return GuidanceStylist.Guidance(title, description, null, null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val yesAction = GuidedAction.Builder(context).apply {
            clickAction(GuidedAction.ACTION_ID_YES)
        }.build()

        val noAction = GuidedAction.Builder(context).apply {
            clickAction(GuidedAction.ACTION_ID_NO)
        }.build()

        actions.add(yesAction)
        actions.add(noAction)
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        val id = action?.id

        if (id == GuidedAction.ACTION_ID_YES) {
            AsyncResyncPrograms().execute()
            add(fragmentManager, EpgSyncLoadingGuidedFragment())
        } else {
            activity?.finish()
        }
    }

    inner class AsyncResyncPrograms : AsyncTask<Void, Void, Void?>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            val contentResolver = activity?.contentResolver
            val channels = ModelUtils.getChannels(contentResolver)

            for (channel in channels) {
                val programsUri = TvContract.buildProgramsUriForChannel(channel.id)
                contentResolver?.delete(programsUri, null, null)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            val inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT)
            EpgSyncJobService.requestImmediateSync(context, inputId, ComponentName(context, TsutaeruJobService::class.java))
        }
    }

}