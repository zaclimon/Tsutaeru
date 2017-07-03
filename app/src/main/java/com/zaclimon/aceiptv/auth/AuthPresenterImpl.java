package com.zaclimon.aceiptv.auth;


import com.zaclimon.aceiptv.util.SharedPreferencesRepository;

/**
 * Concrete implementation of the AuthPresenter interface for the signing up step of the
 * authentication process.
 *
 * @author zaclimon
 * Creation date: 07/06/17
 */

public class AuthPresenterImpl implements AuthPresenter {

     /*
     In order to simplify testing between components, Presenters should not have any dependencies
     to com.android.* classes. In that case, let's reuse interfaces that should technically avoid
     any dependencies with them.
     */

    private AuthView mAuthView;

    /**
     * Default construction of the AuthPresenter implementation.
     * @param view the AuthView used to callback depending on the authentication results.
     */
    public AuthPresenterImpl(AuthView view) {
        mAuthView = view;
    }

    @Override
    public void validateInfo(String username, String password, SharedPreferencesRepository sharedPreferencesRepository) {
        AsyncAuthValidateInfo asyncAuthValidateInfo = new AsyncAuthValidateInfo(username, password, mAuthView, sharedPreferencesRepository);
        asyncAuthValidateInfo.execute();
    }

}
