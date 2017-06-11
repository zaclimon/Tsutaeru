package com.zaclimon.aceiptv.auth;

/**
 * Created by isaac on 17-06-07.
 */

public interface AuthView {

    void onConnectionSuccess();
    void onConnectionFailed(String reason);
    void onPasswordMissing();

}
