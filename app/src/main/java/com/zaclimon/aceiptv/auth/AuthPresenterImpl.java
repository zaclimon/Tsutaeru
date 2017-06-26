package com.zaclimon.aceiptv.auth;


import com.zaclimon.aceiptv.util.SharedPreferencesRepository;

/**
 * Created by isaac on 17-06-07.
 */

public class AuthPresenterImpl implements AuthPresenter {

     /*
     In order to simplify testing between components, Presenters should not have any dependencies
     to com.android.* classes. In that case, let's reuse interfaces that should technically avoid
     any dependencies with them.
     */

    private AuthView mAuthView;

    public AuthPresenterImpl(AuthView view) {
        mAuthView = view;
    }

    @Override
    public void validateInfo(String username, String password, SharedPreferencesRepository sharedPreferencesRepository) {
        AsyncAuthValidateInfo asyncAuthValidateInfo = new AsyncAuthValidateInfo(username, password, mAuthView, sharedPreferencesRepository);
        asyncAuthValidateInfo.execute();
    }

}
