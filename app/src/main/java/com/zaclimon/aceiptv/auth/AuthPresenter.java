package com.zaclimon.aceiptv.auth;

import com.zaclimon.aceiptv.util.SharedPreferencesRepository;

/**
 * Created by isaac on 17-06-07.
 */

public interface AuthPresenter {
    void validateInfo(String username, String password, SharedPreferencesRepository sharedPreferencesRepository);
}
