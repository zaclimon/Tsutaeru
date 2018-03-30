package com.zaclimon.tsutaeru.ui.auth;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.ui.tvinputsetup.TsutaeruTvInputSetupActivity;

import java.util.List;

/**
 * Fifth and last step of the authentication process.
 * <p>
 * Shows the user that he/she is connected and that he/she may now enjoy it's
 * experience with Ace TV.
 *
 * @author zaclimon
 * Creation date: 24/06/17
 */

public class FifthStepAuthGuidedFragment extends GuidedStepSupportFragment {

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.connection_successful_text);
        String breadcrumb = getArguments().getString(SecondStepAuthGuidedFragment.USERNAME_ARGUMENT);
        String description;
        ComponentName componentName = getActivity().getCallingActivity();

        if (componentName != null && componentName.getClassName().equals(TsutaeruTvInputSetupActivity.class.getName())) {
            description = getString(R.string.connected_description_live_channels);
        } else {
            description = getString(R.string.connected_description_standalone);
        }

        return (new GuidanceStylist.Guidance(title, description, breadcrumb, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder exitAction = new GuidedAction.Builder(getActivity());
        exitAction.title(R.string.lets_go_text);
        exitAction.id(GuidedAction.ACTION_ID_OK);
        actions.add(exitAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {
        getActivity().setResult(Activity.RESULT_OK);
        finishGuidedStepSupportFragments();
    }
}
