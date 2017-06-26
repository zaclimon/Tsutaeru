package com.zaclimon.aceiptv.auth;

/**
 * Created by isaac on 17-06-07.
 */

public interface AuthView {

    String getPlaylistLink(String username, String password);
    void onConnectionSuccess();
    void onWrongCredentialsReceived();

}
