package com.zaclimon.acetv.ui.auth;

import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import com.zaclimon.acetv.R;

import java.util.List;

/**
 * First step in the authentication process
 * <p>
 * Shows the potential user a welcome screen requiring him/her to sign-in
 * in order to benefit from Ace TV services.
 *
 * @author zaclimon
 * Creation date: 23/06/17
 */

public class FirstStepAuthGuidedFragment extends GuidedStepSupportFragment {

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.welcome_text);
        String description = getString(R.string.authentication_required);

        return (new GuidanceStylist.Guidance(title, description, null, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder yesAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder noAction = new GuidedAction.Builder(getActivity());
        yesAction.title(R.string.lets_go_text);
        noAction.title(R.string.not_yet_text);
        yesAction.id(GuidedAction.ACTION_ID_OK);
        noAction.id(GuidedAction.ACTION_ID_CANCEL);
        actions.add(yesAction.build());
        actions.add(noAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {

        long id = guidedAction.getId();

        if (id == GuidedAction.ACTION_ID_OK) {
            add(getActivity().getSupportFragmentManager(), new SecondStepAuthGuidedFragment());
        } else if (id == GuidedAction.ACTION_ID_CANCEL) {
            getActivity().finish();
        }
    }
}
