package com.zaclimon.acetv.ui.settings;

/**
 * Interface in which a View can register itself in order to retrieve callbacks when
 * an update for the application is available.
 *
 * @author zaclimon
 * Creation date: 04/08/17
 */

public interface AppUpdateView {

    /**
     * Callback received when a new version of the application is received.
     *
     * @param versionName The new version name of the release
     * @param changelog the various changelog elements of this particular release
     */
    void onNewVersionDetected(String versionName, String[] changelog);

    /**
     * Callback received when the version fetched has not changed.
     */
    void onSameVersionDetected();

    /**
     * Callback received when there has been an error during fetching the changelog.
     */
    void onFetchFailed();

}
