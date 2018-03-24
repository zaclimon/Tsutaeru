package com.zaclimon.acetv.ui.settings;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.utils.TvContractUtils;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.service.AceJobService;
import com.zaclimon.acetv.util.Constants;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Setting option that forces an EPG sync for the next hour.
 *
 * @author zaclimon
 * Creation date: 22/06/17
 */

public class EpgForceSyncGuidedFragment extends GuidedStepSupportFragment {

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.force_epg_sync_title);
        String description = getString(R.string.force_epg_sync_description);

        return (new GuidanceStylist.Guidance(title, description, null, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder yesAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder noAction = new GuidedAction.Builder(getActivity());
        yesAction.clickAction(GuidedAction.ACTION_ID_YES);
        noAction.clickAction(GuidedAction.ACTION_ID_NO);
        actions.add(yesAction.build());
        actions.add(noAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {
        if (guidedAction.getId() == GuidedAction.ACTION_ID_YES) {
            new AsyncResyncPrograms().execute();
            add(getFragmentManager(), new EpgSyncLoadingGuidedFragment());
        } else {
            getActivity().finish();
        }
    }

    /**
     * Private class that removes all programs and then syncs them back up
     *
     * @author zaclimon
     * Creation date: 27/08/17
     */
    private class AsyncResyncPrograms extends AsyncTask<Void, Void, Void> {

        @Override
        public Void doInBackground(Void... params) {

            if (isAdded()) {
                ContentResolver contentResolver = getActivity().getContentResolver();
                List<Channel> channels = TvContractUtils.getChannels(contentResolver);

                for (Channel channel : channels) {
                    Uri channelProgramsUri = TvContract.buildProgramsUriForChannel(channel.getId());
                    contentResolver.delete(channelProgramsUri, null, null);
                }
            }
            return (null);
        }

        @Override
        public void onPostExecute(Void result) {
            String inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT);
            EpgSyncJobService.requestImmediateSync(getActivity(), inputId, TimeUnit.HOURS.toMillis(48), new ComponentName(getActivity(), AceJobService.class));
        }
    }

}
