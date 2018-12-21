package com.zaclimon.tsutaeru.ui.settings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.tv.TvContract
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.widget.TextView
import android.widget.Toast
import com.google.android.media.tv.companionlibrary.sync.EpgSyncJobService
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.util.Constants
import kotlin.math.roundToInt

/**
 * GuidedStepFragment that sync itself with the broadcast intent sent by
 * [com.google.android.media.tv.companionlibrary.sync.EpgSyncJobService]
 *
 * @author zaclimon
 */
class EpgSyncLoadingGuidedFragment : GuidedStepSupportFragment() {

    private val fragmentBroadcastReceiver = LoadingBroadcastReceiver()

    override fun onStart() {
        super.onStart()
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(requireContext()).registerReceiver(fragmentBroadcastReceiver, IntentFilter(EpgSyncJobService.ACTION_SYNC_STATUS_CHANGED))
        val loadingTitle = activity?.findViewById<TextView>(R.id.loading_title)
        loadingTitle?.text = getString(R.string.channel_update, 0)
    }

    override fun onCreateGuidanceStylist(): GuidanceStylist {
        return (object: GuidanceStylist() {
            override fun onProvideLayoutId(): Int {
                return (R.layout.fragment_guided_wizard_loading)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(fragmentBroadcastReceiver)
    }

    private fun updatePercentageStatus(percentage: Int) {
        val loadingTitle = activity?.findViewById<TextView>(R.id.loading_title)
        loadingTitle?.text = getString(R.string.channel_update, percentage)
    }

    /**
     * BroadcastReceiver responsible for reacting to the broadcast sent by
     * [com.google.android.media.tv.companionlibrary.sync.EpgSyncJobService]
     *
     * @author zaclimon
     */
    inner class LoadingBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val inputIdExtra = p1?.getStringExtra(EpgSyncJobService.BUNDLE_KEY_INPUT_ID)
            val tsutaeruInputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT)

            inputIdExtra?.let {
                if (it == tsutaeruInputId) {
                    val syncStatus = p1.getStringExtra(EpgSyncJobService.SYNC_STATUS)

                    if (syncStatus == EpgSyncJobService.SYNC_FINISHED) {
                        Toast.makeText(p0, p0?.getString(R.string.restart_live_channels), Toast.LENGTH_SHORT).show()
                        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(p0!!).unregisterReceiver(this)
                        activity?.finish()
                    } else if (syncStatus == EpgSyncJobService.SYNC_SCANNED) {
                        val channelsCount = p1.getIntExtra(EpgSyncJobService.BUNDLE_KEY_CHANNEL_COUNT, 1)
                        val channelsScanned = p1.getIntExtra(EpgSyncJobService.BUNDLE_KEY_CHANNELS_SCANNED, 0)
                        val percentage = (channelsScanned / channelsCount.toDouble() * 100).roundToInt()
                        updatePercentageStatus(percentage)
                    }
                }
            }
        }
    }
}