package com.zaclimon.aceiptv.auth;

import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.widget.Toast;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.util.SharedPreferencesRepository;
import com.zaclimon.aceiptv.util.SharedPreferencesRepositoryImpl;
import com.zaclimon.aceiptv.util.Constants;

/**
 * Fourth step in the authentication process.
 *
 * It is mostly the core of the process since it is verifying that a given username/password
 * match is valid and saves it for future use.
 *
 * @author zaclimon
 * Creation date: 24/06/17
 */

public class LoadingAuthGuidedFragment extends GuidedStepFragment implements AuthView {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();

        Bundle arguments = getArguments();
        String email = arguments.getString(EmailStepAuthGuidedFragment.EMAIL_ARGUMENT);
        String password = arguments.getString(PasswordStepAuthGuidedFragment.PASSWORD_ARGUMENT);
        AuthPresenter authPresenter = new AuthPresenterImpl(this);
        SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepositoryImpl(getActivity());

        authPresenter.validateInfo(email, password, sharedPreferencesRepository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        return new GuidanceStylist() {
            @Override
            public int onProvideLayoutId() {
                return R.layout.fragment_guided_wizard_loading;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConnectionSuccess() {
        GuidedStepFragment connectedFragment = new ConnectedStepAuthGuidedFragment();
        Bundle bundle = new Bundle();

        bundle.putString(EmailStepAuthGuidedFragment.EMAIL_ARGUMENT, getArguments().getString(EmailStepAuthGuidedFragment.EMAIL_ARGUMENT));
        connectedFragment.setArguments(bundle);
        add(getFragmentManager(), connectedFragment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onWrongCredentialsReceived() {
        Toast.makeText(getActivity(), R.string.wrong_credentials, Toast.LENGTH_LONG).show();
        popBackStackToGuidedStepFragment(PasswordStepAuthGuidedFragment.class, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlaylistLink(String username, String password) {
        // Could be honestly better since it's not the View's role to get the link...
        return (getString(R.string.ace_playlist_url, username, password, Constants.STREAM_TYPE_HLS));
    }
}
