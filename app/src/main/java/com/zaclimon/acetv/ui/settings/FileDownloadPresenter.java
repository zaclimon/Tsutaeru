package com.zaclimon.acetv.ui.settings;

/**
 * Interface used to download a given file and potentially save it.
 *
 * @author zaclimon
 * Creation date: 04/08/17
 */

public interface FileDownloadPresenter {

    /**
     * Downloads a given file
     * @param url the url of the file to download.
     */
    void downloadFile(String url);

}
