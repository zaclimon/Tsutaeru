package com.zaclimon.tsutaeru.repository

import android.content.Context
import com.zaclimon.tsutaeru.util.Constants

/**
 * Implementation of the [SharedPreferencesRepository] interface for Tsutaeru.
 *
 * @author zaclimon
 */
class SharedPreferencesRepositoryImpl(context: Context) : SharedPreferencesRepository {

    private val repositoryPreferences = context.getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)
    private val repositoryEditor = repositoryPreferences.edit()

    override fun getString(key: String): String {
        return repositoryPreferences.getString(key, "")
    }

    override fun getString(key: String, defaultValue: String): String {
        return repositoryPreferences.getString(key, defaultValue)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return repositoryPreferences.getBoolean(key, defaultValue)
    }

    override fun getInt(key: String): Int {
        return repositoryPreferences.getInt(key, 0)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return repositoryPreferences.getInt(key, defaultValue)
    }

    override fun putString(key: String, value: String) {
        repositoryEditor.putString(key, value)
    }

    override fun putBoolean(key: String, value: Boolean) {
        repositoryEditor.putBoolean(key, value)
    }

    override fun putInt(key: String, value: Int) {
        repositoryEditor.putInt(key, value)
    }

    override fun apply() {
        repositoryEditor.apply()
    }
}