package com.zaclimon.tsutaeru.receiver

import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.tv.TvContract
import android.os.Build
import com.google.android.media.tv.companionlibrary.sync.EpgSyncJobService
import com.zaclimon.tsutaeru.service.TsutaeruJobService
import com.zaclimon.tsutaeru.util.Constants

/**
 * Broadcast received that syncs the EPG in a periodic way if it's related job wasn't added
 * during the setup.
 *
 * @author zaclimon
 */
class TsutaeruTvEpgReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        /*
          Based on CumulusTv example found here:
          https://github.com/Fleker/CumulusTV/blob/master/app/src/main/java/com/felkertech/cumulustv/receivers/TvBootReceiver.java
         */

        val jobScheduler = p0?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfos = jobScheduler.allPendingJobs

        // Setup periodic sync if we can't find it
        if (jobInfos.isEmpty()) {
            val inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT)
            EpgSyncJobService.setUpPeriodicSync(p0, inputId, ComponentName(p0, TsutaeruJobService::class.java))
            return
        }

        // Reschedule pending jobs on Lollipop 5.0 and 5.1.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            for (jobinfo in jobInfos) {
                if (jobinfo.isPersisted) {
                    jobScheduler.schedule(jobinfo)
                }
            }
        }
    }

}