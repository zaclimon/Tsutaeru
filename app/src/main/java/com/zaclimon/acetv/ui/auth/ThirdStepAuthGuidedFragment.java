package com.zaclimon.acetv.ui.auth;

import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.InputType;
import android.text.TextUtils;

import com.zaclimon.acetv.R;

import java.util.List;


/**
 * Third step in the authentication process.
 *
 * Asks for a Ace TV user it's password.
 *
 * @author zaclimon
 * Creation date: 23/06/17
 */

public class ThirdStepAuthGuidedFragment extends GuidedStepFragment {

    protected static final String PASSWORD_ARGUMENT = "password";

    private final int ACTION_PASSWORD = 0;

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.enter_password_title);
        return (new GuidanceStylist.Guidance(title, null, null, null));
    }

    @Override
    public void onCreateActions (List<GuidedAction> actions, Bundle savedInstanceState) {

         /*
         Interesting note. For the input type, the type class must be fused with with the variation
         in order to show dots when typing the password.
         */

        GuidedAction.Builder passwordAction = new GuidedAction.Builder(getActivity());
        passwordAction.title(R.string.password_text);
        passwordAction.editTitle("");
        passwordAction.editable(true);
        passwordAction.editInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordAction.id(ACTION_PASSWORD);
        actions.add(passwordAction.build());
    }

    @Override
    public long onGuidedActionEditedAndProceed (GuidedAction action) {

        int id = (int) action.getId();

        if (id == ACTION_PASSWORD) {

            String password = action.getEditTitle().toString();

            if (!TextUtils.isEmpty(password)) {
                GuidedStepFragment loadingFragment = new FourthAuthGuidedFragment();
                Bundle arguments = new Bundle();
                arguments.putString(SecondStepAuthGuidedFragment.USERNAME_ARGUMENT, getArguments().getString(SecondStepAuthGuidedFragment.USERNAME_ARGUMENT));
                arguments.putString(PASSWORD_ARGUMENT, password);
                loadingFragment.setArguments(arguments);
                add(getFragmentManager(), loadingFragment);
            } else {
                action.setTitle(getString(R.string.password_empty));
            }
        }
        return (GuidedAction.ACTION_ID_CURRENT);
    }

}
