package com.zaclimon.tsutaeru.repository

interface SharedPreferencesRepository {

    /**
     * Retrieves a String from the SharedPreferences based on it's key
     *
     * @param key the key of the preference
     * @return the preference string if it exists and an empty string if it doesn't
     */
    fun getString(key: String): String

    /**
     * Retrieves a String from the SharedPreferences based on it's key
     *
     * @param key          the key of the preference
     * @param defaultValue the value if a string isn't found
     * @return the preference string if it exists, otherwise the default value.
     */
    fun getString(key: String, defaultValue: String): String

    /**
     * Retrieves a boolean from the SharedPreferences based on it's key
     *
     * @param key          the key of the preference
     * @param defaultValue the value if the boolean isn't found
     * @return the preference boolean if it exists, otherwise the default value.
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    /**
     * Retrieves a integer from the SharedPreferences based on it's key
     *
     * @param key the key of the preference
     * @return the preference integer if it exists and 0 if it doesn't
     */
    fun getInt(key: String): Int

    /**
     * Retrieves a integer from the SharedPreferences based on it's key
     *
     * @param key          the key of the preference
     * @param defaultValue the value if a integer isn't found
     * @return the preference integer if it exists, otherwise the default value.
     */
    fun getInt(key: String, defaultValue: Int): Int

    /**
     * Sets a String from the SharedPreferences to the given key
     *
     * @param key   the key of the preference
     * @param value the string to be saved
     */
    fun putString(key: String, value: String)

    /**
     * Sets a String from the SharedPreferences to the given key
     *
     * @param key   the key of the preference
     * @param value the integer to be saved
     */
    fun putBoolean(key: String, value: Boolean)

    /**
     * Sets a boolean from the SharedPreferences to the given key
     *
     * @param key   the key of the preference
     * @param value the boolean to be saved
     */
    fun putInt(key: String, value: Int)

    /**
     * Persists all the modifications done by put() methods into the storage
     */
    fun apply()

}