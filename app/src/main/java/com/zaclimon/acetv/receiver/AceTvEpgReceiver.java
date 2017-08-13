package com.zaclimon.acetv.receiver;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Build;

import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.zaclimon.acetv.service.AceJobService;
import com.zaclimon.acetv.util.Constants;

import java.util.List;

/**
 * Broadcast received that syncs the EPG in a periodic way if it's related job wasn't added
 * during the setup.
 *
 * @author zaclimon
 * Creation date: 27/06/17
 */

public class AceTvEpgReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
         /*
          Based on CumulusTv example found here:
          https://github.com/Fleker/CumulusTV/blob/master/app/src/main/java/com/felkertech/cumulustv/receivers/TvBootReceiver.java
         */

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        List<JobInfo> jobInfos = jobScheduler.getAllPendingJobs();

        // Setup periodic sync if we can't find it.
        if (jobInfos.isEmpty()) {
            String inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT);
            EpgSyncJobService.setUpPeriodicSync(context, inputId, new ComponentName(context, AceJobService.class));
            return;
        }

        // Reschedule pending jobs on Lollipop 5.0 and 5.1.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            for (JobInfo jobInfo : jobInfos) {
                if (jobInfo.isPersisted()) {
                    jobScheduler.schedule(jobInfo);
                }
            }
        }
    }

}
