package com.zaclimon.acetv.ui.settings;

/**
 * Concrete implementation of the {@link UserInfoPresenter} interface.
 *
 * @author zaclimon
 * Creation date: 04/07/17
 */

public class UserInfoPresenterImpl implements UserInfoPresenter {

    private UserInfoView mUserInfoView;

    /**
     * Default constructor
     * @param userInfoView the view which will receive the required callbacks.
     */
    public UserInfoPresenterImpl(UserInfoView userInfoView) {
        mUserInfoView = userInfoView;
    }

    @Override
    public void retrieveUserInfo() {
        AsyncRetrieveUserInfos asyncRetrieveUserInfos = new AsyncRetrieveUserInfos(mUserInfoView);
        asyncRetrieveUserInfos.execute();
    }

}
