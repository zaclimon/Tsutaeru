package com.zaclimon.acetv.ui.tvinputsetup;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.media.tv.companionlibrary.ChannelSetupFragment;
import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.service.AceJobService;
import com.zaclimon.acetv.ui.auth.AuthActivityTv;
import com.zaclimon.acetv.util.ActivityUtil;

/**
 * Fragment used to configure channels for Ace TV whether it be on the first
 * run of the Live Channel application or successive runs. It is created. every time
 * a user wants to configure his/her channels.
 *
 * @author zaclimon
 * Creation date: 11/06/17
 */

public class AceTvInputSetupFragment extends ChannelSetupFragment {

    private static final int ASKING_AUTHENTICATION = 0;

    private String mInputId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputId = getActivity().getIntent().getStringExtra(TvInputInfo.EXTRA_INPUT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        ProgressBar progressBar = fragmentView.findViewById(R.id.tune_progress);
        progressBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.colorAccentTv)));
        setChannelListVisibility(true);
        setTitle(R.string.app_name);
        setBadge(getActivity().getDrawable(R.drawable.ace_badge));
        setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryTv));
        return (fragmentView);
    }

    @Override
    public void onScanStarted() {
        if (ActivityUtil.isUsernamePasswordEmpty(getActivity())) {
            // Remove all channels if there were any and authenticate the user.
            ContentResolver contentResolver = getActivity().getContentResolver();
            contentResolver.delete(TvContract.buildChannelsUriForInput(mInputId), null, null);
            Intent authIntent = new Intent(getActivity(), AuthActivityTv.class);
            startActivityForResult(authIntent, ASKING_AUTHENTICATION);
        } else {
            startEpgSync();
        }
    }

    @Override
    public String getInputId() {
        return (mInputId);
    }

    @Override
    public void onScanFinished() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onScanError(int reason) {
        if (reason == EpgSyncJobService.ERROR_NO_CHANNELS) {
            Toast.makeText(getActivity(), R.string.channel_sync_failure, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ASKING_AUTHENTICATION) {
            if (resultCode == Activity.RESULT_OK) {
                startEpgSync();
            } else {
                Toast.makeText(getActivity(), getString(R.string.authentication_not_possible), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
    }

    /**
     * Initially syncs the EPG data for a user. Basing itself from {@link AceJobService} it also
     * sets up a periodic sync.
     */
    private void startEpgSync() {
        EpgSyncJobService.cancelAllSyncRequests(getActivity());
        EpgSyncJobService.requestImmediateSync(getActivity(), mInputId, new ComponentName(getActivity(), AceJobService.class));
        EpgSyncJobService.setUpPeriodicSync(getActivity(), mInputId, new ComponentName(getActivity(), AceJobService.class));
    }
}
