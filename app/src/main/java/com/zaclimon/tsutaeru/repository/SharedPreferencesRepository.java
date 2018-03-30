package com.zaclimon.tsutaeru.repository;

/**
 * Base interface for interacting with SharedPreferences in a Pure MVP fashion.
 * <p>
 * In a pure MVP environment, in order to ensure simplicity in testing, a presenter should not have
 * any direct dependency to Android related packages. Because of this, coupling is reduced in
 * order the environment is modular as well.
 * <p>
 * Please note that the put() methods are only there to set the value and an apply() call is needed
 * to correctly persist the data to the storage. (As defined in {@link android.content.SharedPreferences})
 *
 * @author zaclimon
 * Creation date: 25/06/17
 */

public interface SharedPreferencesRepository {

    /**
     * Retrieves a String from the SharedPreferences based on it's key
     *
     * @param key the key of the preference
     * @return the preference string if it exists
     */
    String getString(String key);

    /**
     * Retrieves a String from the SharedPreferences based on it's key
     *
     * @param key          the key of the preference
     * @param defaultValue the value if a string isn't found
     * @return the preference string if it exists, otherwise the default value.
     */
    String getString(String key, String defaultValue);

    /**
     * Retrieves a boolean from the SharedPreferences based on it's key
     *
     * @param key          the key of the preference
     * @param defaultValue the value if the boolean isn't found
     * @return the preference boolean if it exists, otherwise the default value.
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * Retrieves a integer from the SharedPreferences based on it's key
     *
     * @param key the key of the preference
     * @return the preference integer if it exists
     */
    int getInt(String key);

    /**
     * Retrieves a integer from the SharedPreferences based on it's key
     *
     * @param key          the key of the preference
     * @param defaultValue the value if a integer isn't found
     * @return the preference integer if it exists, otherwise the default value.
     */
    int getInt(String key, int defaultValue);

    /**
     * Sets a String from the SharedPreferences to the given key
     *
     * @param key   the key of the preference
     * @param value the string to be saved
     */
    void putString(String key, String value);

    /**
     * Sets a String from the SharedPreferences to the given key
     *
     * @param key   the key of the preference
     * @param value the integer to be saved
     */
    void putInt(String key, int value);

    /**
     * Sets a boolean from the SharedPreferences to the given key
     *
     * @param key   the key of the preference
     * @param value the boolean to be saved
     */
    void putBoolean(String key, boolean value);

    /**
     * Persists all the modifications done by put() methods into the storage
     */
    void apply();

}
