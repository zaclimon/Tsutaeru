package com.zaclimon.aceiptv.setup;

import android.content.Context;

/**
 * Created by isaac on 17-06-07.
 */

public interface SetupPresenter {
    void validateInfo(String username, String password, Context context);
}
