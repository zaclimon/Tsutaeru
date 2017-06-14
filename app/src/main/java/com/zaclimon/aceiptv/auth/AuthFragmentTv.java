package com.zaclimon.aceiptv.auth;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.util.AceChannelUtil;

/**
 * Created by isaac on 17-06-08.
 */

public class AuthFragmentTv extends Fragment implements AuthView {

    private AuthPresenter presenter;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button connectButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.fragment_auth_tv, container));
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter = new AuthPresenterImpl(this);
        usernameEditText = (EditText) getActivity().findViewById(R.id.setup_username_edittext);
        passwordEditText = (EditText) getActivity().findViewById(R.id.setup_password_edittext);
        connectButton = (Button) getActivity().findViewById(R.id.connect_button);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.validateInfo(usernameEditText.getText().toString(), passwordEditText.getText().toString(), getActivity());
            }
        });

    }

    @Override
    public void onConnectionSuccess() {

        /*
         If the activity was called from another one (For example before syncing EPG data for Live TV)
         return a RESULT_OK value so the said Activity can proceed with it's other tasks.
         */
        ComponentName componentName = getActivity().getCallingActivity();
        if (componentName != null) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }

        Toast.makeText(getActivity(), "Connection sucessfull!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(String reason) {

    }

    @Override
    public void onPasswordMissing() {

    }
}
