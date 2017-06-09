package com.zaclimon.aceiptv.setup;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zaclimon.aceiptv.R;

/**
 * Created by isaac on 17-06-08.
 */

public class SetupFragmentTv extends Fragment implements SetupView {

    private SetupPresenter presenter;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button connectButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.fragment_setup_tv, container));
    }

    @Override
    public void onStart() {
        super.onStart();

        presenter = new SetupPresenterImpl(this);
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

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public void onPasswordMissing() {

    }
}
