package com.zaclimon.tsutaeru.ui.tvinputsetup

import android.content.Intent
import android.media.tv.TvContract
import android.media.tv.TvInputInfo
import android.os.Bundle
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.google.android.media.tv.companionlibrary.setup.ChannelSetupStepSupportFragment
import com.zaclimon.tsutaeru.R
import com.zaclimon.tsutaeru.service.TsutaeruJobService
import com.zaclimon.tsutaeru.ui.auth.AuthActivityTv
import com.zaclimon.tsutaeru.util.ActivityUtil

/**
 * Fragment used to configure channels for a provider whether it be on the first
 * run of the Live Channel application or successive runs. It is created. every time
 * a user wants to configure his/her channels.
 *
 * Concrete implementation of [ChannelSetupStepSupportFragment] for Tsutaeru.
 *
 * @author zaclimon
 */
class TsutaeruTvInputSetupGuidedFragment : ChannelSetupStepSupportFragment<TsutaeruJobService>() {

    companion object {
        private const val REQUEST_AUTHENTICATION = 0
    }

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.tif_channel_setup_title)
        val description = getString(R.string.tif_channel_setup_description)
        val icon = activity?.getDrawable(R.drawable.tsutaeru_badge)

        return GuidanceStylist.Guidance(title, description, null, icon)
    }

    override fun onStart() {

        val activityContext = context

        if (activityContext != null) {
            if (ActivityUtil.areCredentialsEmpty(activityContext)) {
                // Remove all channels if there were any and authenticate the user.
                val contentResolver = context?.contentResolver
                val authIntent = Intent(context, AuthActivityTv::class.java)
                val fragmentInputId = activity?.intent?.getStringExtra(TvInputInfo.EXTRA_INPUT_ID)
                contentResolver?.delete(TvContract.buildChannelsUriForInput(fragmentInputId), null, null)
                startActivityForResult(authIntent, REQUEST_AUTHENTICATION)
            } else {
                super.onStart()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_AUTHENTICATION) {
            if (resultCode == FragmentActivity.RESULT_OK) {
                onStart()
            } else {
                Toast.makeText(context, R.string.authentication_not_possible, Toast.LENGTH_LONG).show()
                activity?.finish()
            }
        }
    }

    override fun getEpgSyncJobServiceClass(): Class<TsutaeruJobService> {
        return TsutaeruJobService::class.java
    }

}