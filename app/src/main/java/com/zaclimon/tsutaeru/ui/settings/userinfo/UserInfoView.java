package com.zaclimon.tsutaeru.ui.settings.userinfo;

import java.util.Date;

/**
 * Interface used to react to actions after retrieving a view.
 *
 * @author zaclimon
 * Creation date: 03/07/17
 */

public interface UserInfoView {

    /**
     * Gets the required API endpoint in order to fetch a user's information
     *
     * @return the URL pointing to the user information
     */
    String getUserInfoApiEndpoint();

    /**
     * Called upon successful connection and information retrieved
     *
     * @param status         the user's account status
     * @param expirationDate the user's account expiration date
     * @param isTrial        whether the user's account type is a trial one
     * @param maxConnections the maximum number of connections possible on the user's account
     */
    void onConnectionSuccess(String status, Date expirationDate, boolean isTrial, int maxConnections);

    /**
     * Called upon connection failure when retrieving the user's account information
     */
    void onConnectionFailed();
}
