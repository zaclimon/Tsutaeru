package com.zaclimon.aceiptv.setup;

/**
 * Created by isaac on 17-06-07.
 */

public interface SetupView {

    void onConnectionSuccess();
    void onConnectionFailed();
    void onPasswordMissing();

}
