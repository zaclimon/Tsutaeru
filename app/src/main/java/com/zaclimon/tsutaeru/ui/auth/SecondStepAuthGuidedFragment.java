package com.zaclimon.tsutaeru.ui.auth;

import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.InputType;
import android.text.TextUtils;

import com.zaclimon.tsutaeru.R;

import java.util.List;

/**
 * Second step in the authentication process.
 * <p>
 * It precisely asks for an Ace TV it's username. In some cases, it is an email address, in
 * others it is a custom one.
 *
 * @author zaclimon
 * Creation date: 23/06/17
 */

public class SecondStepAuthGuidedFragment extends GuidedStepSupportFragment {

    /**
     * Username argument used to pass between GuidedStepFragments in order to get a given value.
     */
    protected static final String USERNAME_ARGUMENT = "username";

    private final int ACTION_USERNAME = 0;

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.enter_username_title);
        return (new GuidanceStylist.Guidance(title, null, null, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder usernameAction = new GuidedAction.Builder(getActivity());
        usernameAction.title(R.string.username_text);
        usernameAction.editTitle("");
        usernameAction.editable(true);
        usernameAction.editInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        usernameAction.id(ACTION_USERNAME);
        actions.add(usernameAction.build());
    }

    @Override
    public long onGuidedActionEditedAndProceed(GuidedAction action) {

        int id = (int) action.getId();

        if (id == ACTION_USERNAME) {

            String username = action.getEditTitle().toString();

            if (!TextUtils.isEmpty(username)) {
                GuidedStepSupportFragment passwordFragment = new ThirdStepAuthGuidedFragment();
                Bundle arguments = new Bundle();
                arguments.putString(USERNAME_ARGUMENT, username.trim());
                passwordFragment.setArguments(arguments);
                add(getFragmentManager(), passwordFragment);
            } else {
                action.setTitle(getString(R.string.username_empty));
            }
        }
        return (GuidedAction.ACTION_ID_CURRENT);
    }
}
