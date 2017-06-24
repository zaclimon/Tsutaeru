package com.zaclimon.aceiptv.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.util.Constants;

import java.util.List;

/**
 * Created by isaac on 17-06-24.
 */

public class LoadingAuthGuidedFragment extends GuidedStepFragment implements AuthView {

    @Override
    public void onStart() {
        super.onStart();

        Bundle arguments = getArguments();
        String email = arguments.getString(EmailStepAuthGuidedFragment.EMAIL_ARGUMENT);
        String password = arguments.getString(PasswordStepAuthGuidedFragment.PASSWORD_ARGUMENT);
        AuthPresenter authPresenter = new AuthPresenterImpl(this);

        authPresenter.validateInfo(email, password, getActivity());
    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        return new GuidanceStylist() {
            @Override
            public int onProvideLayoutId() {
                return R.layout.fragment_guided_wizard_loading;
            }
        };
    }

    @Override
    public void onConnectionSuccess() {
        GuidedStepFragment connectedFragment = new ConnectedStepAuthGuidedFragment();
        Bundle bundle = new Bundle();

        bundle.putString(EmailStepAuthGuidedFragment.EMAIL_ARGUMENT, getArguments().getString(EmailStepAuthGuidedFragment.EMAIL_ARGUMENT));
        connectedFragment.setArguments(bundle);
        add(getFragmentManager(), connectedFragment);
    }

    @Override
    public void onConnectionFailed(String reason) {
        Toast.makeText(getActivity(), reason, Toast.LENGTH_LONG).show();
        popBackStackToGuidedStepFragment(PasswordStepAuthGuidedFragment.class, 0);
    }
}
