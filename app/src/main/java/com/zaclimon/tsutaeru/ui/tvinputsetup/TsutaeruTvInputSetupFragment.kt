package com.zaclimon.tsutaeru.ui.tvinputsetup

import android.content.ComponentName
import android.content.Intent
import android.content.res.ColorStateList
import android.media.tv.TvContract
import android.media.tv.TvInputInfo
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.media.tv.companionlibrary.setup.ChannelSetupFragment
import com.google.android.media.tv.companionlibrary.sync.EpgSyncJobService
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.service.TsutaeruJobService
import com.zaclimon.tsutaeru.ui.auth.AuthActivityTv
import com.zaclimon.tsutaeru.util.ActivityUtil
import com.zaclimon.tsutaeru.util.Constants

class TsutaeruTvInputSetupFragment : ChannelSetupFragment() {

    private lateinit var fragmentInputId: String

    companion object {
        private const val REQUEST_AUTHENTICATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentInputId = activity?.intent?.getStringExtra(TvInputInfo.EXTRA_INPUT_ID) ?: TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = super.onCreateView(inflater, container, savedInstanceState)
        val progressBar = fragmentView?.findViewById<ProgressBar>(R.id.tune_progress)
        progressBar?.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(inflater.context, R.color.colorAccentTv))
        setChannelListVisibility(true)
        setTitle(R.string.app_name)
        setBadge(context?.getDrawable(R.drawable.ace_badge))
        setBackgroundColor(ContextCompat.getColor(inflater.context, R.color.colorPrimaryTv))
        fragmentInputId = activity?.intent?.getStringExtra(TvInputInfo.EXTRA_INPUT_ID) ?: TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT)
        return fragmentView
    }

    override fun onScanStarted() {
        if (ActivityUtil.isUsernamePasswordEmpty(context)) {
            // Remove all channels if there were any and authenticate the user.
            val contentResolver = context?.contentResolver
            val authIntent = Intent(context, AuthActivityTv::class.java)
            contentResolver?.delete(TvContract.buildChannelsUriForInput(fragmentInputId), null, null)
            startActivityForResult(authIntent, REQUEST_AUTHENTICATION)
        }
    }

    override fun getInputId(): String {
        return fragmentInputId
    }

    override fun onScanFinished() {
        activity?.setResult(FragmentActivity.RESULT_OK)
        activity?.finish()
    }

    override fun onScanError(reason: Int) {
        if (reason == EpgSyncJobService.ERROR_NO_CHANNELS) {
            Toast.makeText(context, R.string.channel_sync_failure, Toast.LENGTH_SHORT).show()
            activity?.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_AUTHENTICATION) {
            if (resultCode == FragmentActivity.RESULT_OK) {
                startEpgSync()
            } else {
                Toast.makeText(context, R.string.authentication_not_possible, Toast.LENGTH_LONG).show()
                activity?.finish()
            }
        }
    }

    /**
     * Initially syncs the EPG data for a user. Basing itself from [TsutaeruJobService] it also
     * sets up a periodic sync.
     */
    private fun startEpgSync() {
        EpgSyncJobService.cancelAllSyncRequests(context)
        EpgSyncJobService.requestImmediateSync(context, fragmentInputId, ComponentName(context, TsutaeruJobService::class.java))
        EpgSyncJobService.setUpPeriodicSync(context, fragmentInputId, ComponentName(context, TsutaeruJobService::class.java))
    }
}