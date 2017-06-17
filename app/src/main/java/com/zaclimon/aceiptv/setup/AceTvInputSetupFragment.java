package com.zaclimon.aceiptv.setup;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.media.tv.TvInputInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.media.tv.companionlibrary.ChannelSetupFragment;
import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.auth.AuthActivityTv;
import com.zaclimon.aceiptv.service.AceJobService;

/**
 * Created by isaac on 17-06-11.
 */

public class AceTvInputSetupFragment extends ChannelSetupFragment {

    private static final int ASKING_AUTHENTICATION = 0;

    private boolean mErrorFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        setChannelListVisibility(true);
        return fragmentView;
    }

    @Override
    public void onScanStarted() {
        Intent authIntent = new Intent(getActivity(), AuthActivityTv.class);
        startActivityForResult(authIntent, ASKING_AUTHENTICATION);
    }

    @Override
    public String getInputId() {
        return (null);
    }

    @Override
    public void onScanFinished() {
        if (!mErrorFound) {
            EpgSyncJobService.cancelAllSyncRequests(getActivity());
            getActivity().setResult(Activity.RESULT_OK);
        } else {
            getActivity().setResult(Activity.RESULT_CANCELED);
        }
        getActivity().finish();
    }

    @Override
    public void onScanError(int reason) {
        mErrorFound = true;
        Log.d(getClass().getSimpleName(), "Reason: " + reason);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ASKING_AUTHENTICATION) {
            if (resultCode == Activity.RESULT_OK) {
                String inputId = getActivity().getIntent().getStringExtra(TvInputInfo.EXTRA_INPUT_ID);
                EpgSyncJobService.cancelAllSyncRequests(getActivity());
                EpgSyncJobService.requestImmediateSync(getActivity(), inputId, new ComponentName(getActivity(), AceJobService.class));
            } else {
                Toast.makeText(getActivity(), getString(R.string.authentication_not_possible), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }

    }
}
