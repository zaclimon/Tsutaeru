package com.zaclimon.aceiptv.ui.setup;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.media.tv.companionlibrary.ChannelSetupFragment;
import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.ui.auth.AuthActivityTv;
import com.zaclimon.aceiptv.service.AceJobService;
import com.zaclimon.aceiptv.util.ActivityUtil;

/**
 * Fragment used to configure channels for A.C.E. IPTV whether it be on the first
 * run of the Live Channel application or successive runs. It is created. every time
 * a user wants to configure his/her channels.
 *
 * @author zaclimon
 * Creation date: 11/06/17
 */

public class AceTvInputSetupFragment extends ChannelSetupFragment {

    private static final int ASKING_AUTHENTICATION = 0;

    private boolean mErrorFound;
    private String mInputId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputId = getActivity().getIntent().getStringExtra(TvInputInfo.EXTRA_INPUT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        setChannelListVisibility(true);
        setTitle(R.string.app_name);
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
            startFirstSync();
        }
    }

    @Override
    public String getInputId() {
        return (mInputId);
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
                startFirstSync();
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
    private void startFirstSync() {
        EpgSyncJobService.cancelAllSyncRequests(getActivity());
        EpgSyncJobService.requestImmediateSync(getActivity(), mInputId, new ComponentName(getActivity(), AceJobService.class));
        EpgSyncJobService.setUpPeriodicSync(getActivity(), mInputId, new ComponentName(getActivity(), AceJobService.class));
    }
}
