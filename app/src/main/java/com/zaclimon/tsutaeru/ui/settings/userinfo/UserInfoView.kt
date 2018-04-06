package com.zaclimon.tsutaeru.ui.settings.userinfo

import java.util.*

/**
 * Interface used to react to actions after retrieving a user's account information.
 *
 * @author zaclimon
 */
interface UserInfoView {

    /**
     * Gets the required API endpoint in order to fetch a user's information
     *
     * @return the URL pointing to the user information
     */
    fun getUserInfoApiEndpoint(): String

    /**
     * Called upon successful connection and information retrieved
     *
     * @param status         the user's account status
     * @param expirationDate the user's account expiration date
     * @param isTrial        whether the user's account type is a trial one
     * @param maxConnections the maximum number of connections possible on the user's account
     */
    fun onConnectionSuccess(status: String, expirationDate: Date, isTrial: Boolean, maxConnections: Int)

    /**
     * Called upon connection failure when retrieving the user's account information
     */
    fun onConnectionFailed()
}