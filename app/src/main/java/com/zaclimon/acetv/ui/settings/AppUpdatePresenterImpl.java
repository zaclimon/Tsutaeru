package com.zaclimon.acetv.ui.settings;

/**
 * Concrete implementation of {@link AppUpdatePresenter} for Ace TV.
 *
 * @author zaclimon
 * Creation date: 04/08/17
 */

public class AppUpdatePresenterImpl implements AppUpdatePresenter {

    private AppUpdateView mAppUpdateView;

    /**
     * Default constructor for this implementation
     * @param appUpdateView the View which will react to the different callbacks
     */
    public AppUpdatePresenterImpl(AppUpdateView appUpdateView) {
        mAppUpdateView = appUpdateView;
    }

    @Override
    public void fetchUpdate() {
        new AsyncDownloadChangelog(mAppUpdateView).execute();
    }

}
