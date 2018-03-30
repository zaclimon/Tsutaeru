package com.zaclimon.tsutaeru.ui.auth;

import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.widget.TextView;
import android.widget.Toast;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.repository.SharedPreferencesRepository;
import com.zaclimon.tsutaeru.repository.SharedPreferencesRepositoryImpl;
import com.zaclimon.tsutaeru.util.Constants;

/**
 * Fourth step in the authentication process.
 * <p>
 * It is mostly the core of the process since it is verifying that a given username/password
 * match is valid and saves it for future use.
 *
 * @author zaclimon
 * Creation date: 24/06/17
 */

public class FourthAuthGuidedFragment extends GuidedStepSupportFragment implements AuthView {

    @Override
    public void onStart() {
        super.onStart();

        Bundle arguments = getArguments();
        String username = arguments.getString(SecondStepAuthGuidedFragment.USERNAME_ARGUMENT);
        String password = arguments.getString(ThirdStepAuthGuidedFragment.PASSWORD_ARGUMENT);
        TextView loadingTitle = getActivity().findViewById(R.id.loading_title);
        AuthPresenter authPresenter = new AuthPresenterImpl(this);
        SharedPreferencesRepository sharedPreferencesRepository = new SharedPreferencesRepositoryImpl(getActivity());

        loadingTitle.setText(R.string.wont_be_long_text);
        authPresenter.validateInfo(username, password, sharedPreferencesRepository);
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
        GuidedStepSupportFragment connectedFragment = new FifthStepAuthGuidedFragment();
        Bundle bundle = new Bundle();

        bundle.putString(SecondStepAuthGuidedFragment.USERNAME_ARGUMENT, getArguments().getString(SecondStepAuthGuidedFragment.USERNAME_ARGUMENT));
        connectedFragment.setArguments(bundle);
        add(getFragmentManager(), connectedFragment);
    }

    @Override
    public void onWrongCredentialsReceived() {
        Toast.makeText(getActivity(), R.string.wrong_credentials_toast, Toast.LENGTH_LONG).show();
        popBackStackToGuidedStepSupportFragment(ThirdStepAuthGuidedFragment.class, 0);
    }

    @Override
    public String getPlaylistLink(String username, String password) {
        // Could be honestly better since it's not the View's role to get the link...
        return (getString(R.string.ace_playlist_url, username, password, Constants.STREAM_TYPE_HLS));
    }

    @Override
    public void onTimeoutReceived() {
        Toast.makeText(getActivity(), R.string.connection_timeout_toast, Toast.LENGTH_SHORT).show();
        popBackStackToGuidedStepSupportFragment(ThirdStepAuthGuidedFragment.class, 0);
    }
}
