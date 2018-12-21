package com.zaclimon.tsutaeru.ui.auth

/**
 * Reference interface used for a View to react when there are results for an
 * authentication
 *
 * @author zaclimon
 */
interface AuthView {

    /**
     * Callback method used to let the view know when the authentication is successful.
     *
     * @param isAccountChanged is true if the account used to connect has changed from a previous one
     */
    fun onConnectionSuccess(isAccountChanged: Boolean)

    /**
     * Callback method used to let the view know when an error related to the user's credentials
     * are not valid.
     */
    fun onConnectionFailed()

    /**
     * Callback method used to let the view know if there was a timeout on the server side.
     */
    fun onTimeoutReceived()

}