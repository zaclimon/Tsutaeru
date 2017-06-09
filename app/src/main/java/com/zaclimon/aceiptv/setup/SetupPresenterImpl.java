package com.zaclimon.aceiptv.setup;

/**
 * Created by isaac on 17-06-07.
 */

public class SetupPresenterImpl implements SetupPresenter {

    private SetupView setupView;
    private String m3uPlaylist;
    private String epgData;

    public SetupPresenterImpl(SetupView view) {
        setupView = view;
    }

    @Override
    public void validateInfo(String username, String password) {
        if (password != null && !password.isEmpty()) {

        } else {
            setupView.onPasswordMissing();
        }
    }

}
