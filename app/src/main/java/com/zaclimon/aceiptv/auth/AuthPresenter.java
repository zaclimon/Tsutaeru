package com.zaclimon.aceiptv.auth;

import android.content.Context;

/**
 * Created by isaac on 17-06-07.
 */

public interface AuthPresenter {
    void validateInfo(String username, String password, Context context);
}
