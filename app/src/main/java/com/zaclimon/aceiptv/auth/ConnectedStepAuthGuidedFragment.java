package com.zaclimon.aceiptv.auth;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.setup.AceTvInputSetupActivity;

import java.util.List;

/**
 * Fifth and last step of the authentication process.
 *
 * Shows the user that he/she is connected and that he/she may now enjoy it's
 * experience with A.C.E. IPTV.
 *
 * @author zaclimon
 * Creation date: 24/06/17
 */

public class ConnectedStepAuthGuidedFragment extends GuidedStepFragment {

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreateActions (List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder exitAction = new GuidedAction.Builder(getActivity());
        exitAction.title(R.string.lets_go_text);
        exitAction.id(GuidedAction.ACTION_ID_OK);
        actions.add(exitAction.build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {
        getActivity().setResult(Activity.RESULT_OK);
        finishGuidedStepFragments();
    }
}
