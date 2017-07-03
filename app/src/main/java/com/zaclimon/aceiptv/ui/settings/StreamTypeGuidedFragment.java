package com.zaclimon.aceiptv.ui.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.widget.Toast;

import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.service.AceJobService;
import com.zaclimon.aceiptv.util.AceChannelUtil;
import com.zaclimon.aceiptv.util.Constants;

import java.util.List;


/**
 * Setting option that lets the user choose between either the MPEG-TS or the HLS
 * streaming format.
 *
 * @author zaclimon
 * Creation date: 21/06/17
 */

public class StreamTypeGuidedFragment extends GuidedStepFragment {

    private final int ACTION_MPEG_TS = 0;
    private final int ACTION_HLS = 1;

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ACE_IPTV_PREFERENCES, Context.MODE_PRIVATE);
        String streamType = sharedPreferences.getString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS);
        String title = getString(R.string.stream_type);
        String description = getString(R.string.stream_type_description);
        String breadcrumb;

        if (streamType.equals(Constants.STREAM_TYPE_MPEG_TS)) {
            breadcrumb = getString(R.string.current_status_text, getString(R.string.mpeg_ts_text));
        } else {
            breadcrumb = getString(R.string.current_status_text, getString(R.string.hls_text));
        }

        return (new GuidanceStylist.Guidance(title, description, breadcrumb, null));
    }

    @Override
    public void onCreateActions (List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder tsAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder hlsAction = new GuidedAction.Builder(getActivity());
        hlsAction.title(R.string.hls_text);
        tsAction.title(R.string.mpeg_ts_text);
        hlsAction.id(ACTION_HLS);
        tsAction.id(ACTION_MPEG_TS);
        actions.add(hlsAction.build());
        actions.add(tsAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ACE_IPTV_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String initialAction = sharedPreferences.getString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS);
        int action = (int) guidedAction.getId();

        switch (action) {
            case ACTION_HLS:
                editor.putString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS);
                break;
            case ACTION_MPEG_TS:
                editor.putString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_MPEG_TS);
                break;
            default:
                editor.putString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS);
        }

        editor.apply();

        String modifiedAction = sharedPreferences.getString(Constants.STREAM_TYPE_PREFERENCE, Constants.STREAM_TYPE_HLS);

        if (!initialAction.equals(modifiedAction)) {
            // Sync the channels to reflect the latest changes only if the stream type is different from before.
            String inputId = TvContract.buildInputId(AceChannelUtil.TV_INPUT_SERVICE_COMPONENT);
            EpgSyncJobService.requestImmediateSync(getActivity(), inputId, new ComponentName(getActivity(), AceJobService.class));
            Toast.makeText(getActivity(), R.string.channel_update, Toast.LENGTH_LONG).show();
        }

        getActivity().finish();
    }

}
