package com.zaclimon.acetv.ui.settings.appupdate;

/**
 * Interface for a given View to interact with a {@link FileDownloadPresenter} in order to download
 * a file.
 *
 * @author zaclimon
 * Creation date: 04/08/17
 */

public interface FileDownloadView {

    /**
     * Verifies if the application has the permission to access the external storage
     * @return true if the application can save files
     */
    boolean checkPermissions();

    /**
     * Send a request to allow storage permissions for the application.
     */
    void askPermissions();

    /**
     * Callback sent if a download has been successful.
     * @param path the path of the downloaded file.
     */
    void onDownloadSuccess(String path);

    /**
     * Callback sent if the download failed for some reason.
     */
    void onDownloadFailed();

}
