package com.zaclimon.aceiptv.auth;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.InputType;
import android.util.Log;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.main.MainActivity;
import com.zaclimon.aceiptv.service.AceInputTvService;
import com.zaclimon.aceiptv.setup.AceTvInputSetupActivity;

import java.util.List;

/**
 * Created by isaac on 17-06-24.
 */

public class ConnectedStepAuthGuidedFragment extends GuidedStepFragment {

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.connection_successful_text);
        String breadcrumb = getArguments().getString(EmailStepAuthGuidedFragment.EMAIL_ARGUMENT);
        String description;
        ComponentName componentName = getActivity().getCallingActivity();

        if (componentName != null && componentName.getClassName().equals(AceTvInputSetupActivity.class.getName())) {
            description = getString(R.string.connected_description_live_channels);
        } else {
            description = getString(R.string.connected_description_standalone);
        }

        return (new GuidanceStylist.Guidance(title, description, breadcrumb, null));
    }

    @Override
    public void onCreateActions (List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder exitAction = new GuidedAction.Builder(getActivity());
        exitAction.title(R.string.lets_go_text);
        exitAction.id(GuidedAction.ACTION_ID_OK);
        actions.add(exitAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {
        getActivity().setResult(Activity.RESULT_OK);
        finishGuidedStepFragments();
    }
}
