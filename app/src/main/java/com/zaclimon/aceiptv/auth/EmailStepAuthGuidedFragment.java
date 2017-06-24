package com.zaclimon.aceiptv.auth;

import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.InputType;
import android.util.Patterns;

import com.zaclimon.aceiptv.R;

import java.util.List;

/**
 * Created by isaac on 17-06-23.
 */

public class EmailStepAuthGuidedFragment extends GuidedStepFragment {

    protected static final String EMAIL_ARGUMENT = "email";

    private final int ACTION_EMAIL_ADDRESS = 0;

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.enter_email_title);
        return (new GuidanceStylist.Guidance(title, null, null, null));
    }

    @Override
    public void onCreateActions (List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder emailAction = new GuidedAction.Builder(getActivity());
        emailAction.title(R.string.email_text);
        emailAction.editTitle("");
        emailAction.editable(true);
        emailAction.editInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailAction.id(ACTION_EMAIL_ADDRESS);
        actions.add(emailAction.build());
    }

    @Override
    public long onGuidedActionEditedAndProceed (GuidedAction action) {

        int id = (int) action.getId();

        if (id == ACTION_EMAIL_ADDRESS) {

            String emailAddress = action.getEditTitle().toString();

            if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                GuidedStepFragment passwordFragment = new PasswordStepAuthGuidedFragment();
                Bundle arguments = new Bundle();
                arguments.putString(EMAIL_ARGUMENT, emailAddress);
                passwordFragment.setArguments(arguments);
                add(getFragmentManager(), passwordFragment);
            } else {
                action.setTitle(getString(R.string.email_not_valid));
            }
        }
        return (GuidedAction.ACTION_ID_CURRENT);
    }
}
