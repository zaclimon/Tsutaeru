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

public class FourthAuthGuidedFragment extends GuidedStepFragment implements AuthView {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();

        Bundle arguments = getArguments();
        String username = arguments.getString(SecondStepAuthGuidedFragment.USERNAME_ARGUMENT);
        String password = arguments.getString(ThirdStepAuthGuidedFragment.PASSWORD_ARGUMENT);
        AuthPresenter authPresenter = new AuthPresenterImpl(this);
        SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepositoryImpl(getActivity());

        authPresenter.validateInfo(username, password, sharedPreferencesRepository);
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
        GuidedStepFragment connectedFragment = new FifthStepAuthGuidedFragment();
        Bundle bundle = new Bundle();

        bundle.putString(SecondStepAuthGuidedFragment.USERNAME_ARGUMENT, getArguments().getString(SecondStepAuthGuidedFragment.USERNAME_ARGUMENT));
        connectedFragment.setArguments(bundle);
        add(getFragmentManager(), connectedFragment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onWrongCredentialsReceived() {
        Toast.makeText(getActivity(), R.string.wrong_credentials_toast, Toast.LENGTH_LONG).show();
        popBackStackToGuidedStepFragment(ThirdStepAuthGuidedFragment.class, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlaylistLink(String username, String password) {
        // Could be honestly better since it's not the View's role to get the link...
        return (getString(R.string.ace_playlist_url, username, password, Constants.STREAM_TYPE_HLS));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTimeoutReceived() {
        Toast.makeText(getActivity(), R.string.connection_timeout_toast, Toast.LENGTH_SHORT).show();
        popBackStackToGuidedStepFragment(ThirdStepAuthGuidedFragment.class, 0);
    }
}
