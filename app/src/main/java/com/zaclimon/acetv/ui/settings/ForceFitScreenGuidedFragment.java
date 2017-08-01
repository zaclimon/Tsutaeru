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
 * Created by isaac on 17-07-31.
 */

public class ForceFitScreenGuidedFragment extends GuidedStepFragment {

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);

        String title = getString(R.string.force_video_fit_title);
        String description = getString(R.string.force_video_fit_description);
        String breadcrumb;
        boolean forceFitActivated = sharedPreferences.getBoolean(Constants.VIDEO_FIT_SCREEN_PREFERENCE, false);

        if (forceFitActivated) {
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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (guidedAction.getId() == GuidedAction.ACTION_ID_YES) {
            editor.putBoolean(Constants.VIDEO_FIT_SCREEN_PREFERENCE, true);
        } else {
            editor.putBoolean(Constants.VIDEO_FIT_SCREEN_PREFERENCE, false);
        }

        editor.apply();
        getActivity().finish();
    }

}
