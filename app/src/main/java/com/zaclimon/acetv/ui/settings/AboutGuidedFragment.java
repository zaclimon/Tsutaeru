package com.zaclimon.acetv.ui.settings;

import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import com.zaclimon.acetv.BuildConfig;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.ui.settings.userinfo.UserInfoGuidedFragment;

import java.util.List;

/**
 * Fragment that shows the about section of the app the user information.
 *
 * @author zaclimon
 * Creation date: 03/07/17
 */

public class AboutGuidedFragment extends GuidedStepSupportFragment {

    private final int USER_INFO_ACTION = 0;

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        String title = getString(R.string.app_name);
        String description = getString(R.string.version_text, BuildConfig.VERSION_NAME);

        return (new GuidanceStylist.Guidance(title, description, null, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder userInfoAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder okAction = new GuidedAction.Builder(getActivity());

        userInfoAction.title(R.string.user_info_text);
        userInfoAction.id(USER_INFO_ACTION);
        okAction.clickAction(GuidedAction.ACTION_ID_OK);

        actions.add(userInfoAction.build());
        actions.add(okAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {

        int id = (int) guidedAction.getId();

        if (id == USER_INFO_ACTION) {
            add(getFragmentManager(), new UserInfoGuidedFragment());
        } else {
            getActivity().finish();
        }

    }

}
