package com.zaclimon.aceiptv.util;

/**
 * Created by isaac on 17-06-25.
 */

public interface SharedPreferencesRepository {

    String getString(String key);
    String getString(String key, String defaultValue);
    boolean getBoolean(String key, boolean defaultValue);
    int getInt(String key);
    int getInt(String key, int defaultValue);
    void putString(String key, String value);
    void putInt(String key, int value);
    void putBoolean(String key, boolean value);
    void apply();

}
