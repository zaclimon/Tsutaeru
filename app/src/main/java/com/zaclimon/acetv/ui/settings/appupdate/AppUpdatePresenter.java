package com.zaclimon.acetv.ui.settings.appupdate;

/**
 * Presenter used to fetch updates for the application.
 *
 * @author zaclimon
 * Creation date: 04/08/17
 */

public interface AppUpdatePresenter {

    /**
     * Fetches an update server to see if there is a new version of the app available.
     */
    void fetchUpdate();

}
