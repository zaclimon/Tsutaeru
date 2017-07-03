package com.zaclimon.aceiptv.ui.auth;

/**
 * Reference interface used for a View to react when there are results for an
 * authentication
 *
 * @author zaclimon
 * Creation date: 07/06/17
 */

public interface AuthView {

    /**
     * Gets a custom playlist link based on the username and the password of a given user.
     * @param username the user's username
     * @param password the user's password
     * @return the required playlist link for a given user
     */
    String getPlaylistLink(String username, String password);

    /**
     * Callback method used to let the view know when the authentication is successful.
     */
    void onConnectionSuccess();

    /**
     * Callback method used to let the view know when an error related to the user's credentials
     * are not valid.
     */
    void onWrongCredentialsReceived();

    /**
     * Callback method used to let the view know if there was a timeout on the server side.
     */
    void onTimeoutReceived();
}
