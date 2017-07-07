package com.zaclimon.acetv.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.zaclimon.acetv.util.Constants;

/**
 * Implementation of the {@link SharedPreferencesRepository} interface for Ace TV.
 *
 * @author zaclimon
 * Creation date: 25/06/17
 */

public class SharedPreferencesRepositoryImpl implements SharedPreferencesRepository {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    /**
     * Base constructor for this class
     * @param context is the context needed to retrieve the SharedPreferences instance.
     */
    public SharedPreferencesRepositoryImpl(Context context) {
        mSharedPreferences = context.getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    /**
     * {@inheritDoc}
     * Please note that the default value is an empty string. ("")
     */
    @Override
    public String getString(String key) {
        return (getString(key, ""));
    }

    @Override
    public String getString(String key, String defaultValue) {
        return (mSharedPreferences.getString(key, defaultValue));
    }

    @Override
    public int getInt(String key) {
        return (getInt(key, 0));
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return (mSharedPreferences.getInt(key, defaultValue));
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return (mSharedPreferences.getBoolean(key, defaultValue));
    }

    @Override
    public void putString(String key, String value) {
        mEditor.putString(key, value);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        mEditor.putInt(key, value);
    }

    @Override
    public void apply() {
        mEditor.apply();
    }

}
