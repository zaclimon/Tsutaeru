package com.zaclimon.tsutaeru.ui.settings.userinfo

/**
 * Concrete implementation of the [UserInfoPresenter] interface.
 *
 * @property userInfoView The view that will be used to display the user information
 *
 * @author zaclimon
 */
class UserInfoPresenterImpl(endpoint: String?, view: UserInfoView) : UserInfoPresenter {

    private val userEndpoint = endpoint
    private val userInfoView = view

    override fun retrieveUserInfo() {
        val asyncRetrieveUserInfo = AsyncRetrieveUserInfo(userEndpoint, userInfoView)
        asyncRetrieveUserInfo.execute()
    }

}