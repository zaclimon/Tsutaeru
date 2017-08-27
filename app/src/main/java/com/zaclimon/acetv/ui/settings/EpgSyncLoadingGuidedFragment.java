package com.zaclimon.acetv.ui.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.tv.TvContract;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.util.Constants;

/**
 * GuidedStepFragment that sync itself with the broadcast intent sent by
 * {@link com.google.android.media.tv.companionlibrary.EpgSyncJobService}
 *
 * @author zaclimon
 * Creation date: 06/08/17
 */

public class EpgSyncLoadingGuidedFragment extends GuidedStepFragment {

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new LoadingBroadcastReceiver(), new IntentFilter(EpgSyncJobService.ACTION_SYNC_STATUS_CHANGED));
        TextView loadingTitle = getActivity().findViewById(R.id.loading_title);
        loadingTitle.setText(R.string.channel_update);
    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        return new GuidanceStylist() {
            @Override
            public int onProvideLayoutId() {
                return R.layout.fragment_guided_wizard_loading;
            }
        };
    }

    /**
     * BroadcastReceiver responsible for reacting to the broadcast sent by
     * {@link com.google.android.media.tv.companionlibrary.EpgSyncJobService}
     *
     * @author zaclimon
     * Creation date: 06/08/17
     */
    private class LoadingBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String inputIdExtra = intent.getStringExtra(EpgSyncJobService.BUNDLE_KEY_INPUT_ID);
            String aceInputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT);

            if (inputIdExtra != null && inputIdExtra.equals(aceInputId)) {
                String syncStatus = intent.getStringExtra(EpgSyncJobService.SYNC_STATUS);

                if (syncStatus.equals(EpgSyncJobService.SYNC_FINISHED)) {
                    Toast.makeText(context, R.string.restart_live_channels, Toast.LENGTH_SHORT).show();
                    LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }

            }
        }
    }


}
