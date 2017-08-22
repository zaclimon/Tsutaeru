package com.zaclimon.acetv.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import com.zaclimon.acetv.R;
import com.zaclimon.acetv.util.Constants;

import java.util.List;

/**
 * Settings section that enables one to use an installed player instead of the integrated one
 * from the application.
 *
 * @author zaclimon
 * Creation date: 22/08/17.
 */

public class VodExternalPlayerGuidedFragment extends GuidedStepFragment {

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.external_player_title);
        String description = getString(R.string.external_player_description);
        String breadcrumb;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);
        boolean isActivated = sharedPreferences.getBoolean(Constants.EXTERNAL_PLAYER_PREFERENCE, false);

        if (isActivated) {
            breadcrumb = getString(R.string.current_status_text, getString(R.string.activated_text));
        } else {
            breadcrumb = getString(R.string.current_status_text, getString(R.string.deactivated_text));
        }

        return (new GuidanceStylist.Guidance(title, description, breadcrumb, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder yesAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder noAction = new GuidedAction.Builder(getActivity());
        yesAction.title(R.string.yes_text);
        noAction.title(R.string.no_text);
        yesAction.id(GuidedAction.ACTION_ID_YES);
        noAction.id(GuidedAction.ACTION_ID_NO);
        actions.add(yesAction.build());
        actions.add(noAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE).edit();

        if (guidedAction.getId() == GuidedAction.ACTION_ID_YES) {
            editor.putBoolean(Constants.EXTERNAL_PLAYER_PREFERENCE, true);
        } else {
            editor.putBoolean(Constants.EXTERNAL_PLAYER_PREFERENCE, false);
        }

        editor.apply();
        getActivity().finish();
    }

}
