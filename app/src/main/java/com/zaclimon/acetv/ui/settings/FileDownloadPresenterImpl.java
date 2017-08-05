package com.zaclimon.acetv.ui.settings;

/**
 * Concrete implementation of {@link FileDownloadPresenter} for Ace TV.
 *
 * @author zaclimon
 * Creation date: 04/08/17
 */

public class FileDownloadPresenterImpl implements FileDownloadPresenter {

    private FileDownloadView mFileDownloadView;

    /**
     * Default constructor for this implementation
     * @param fileDownloadView the View which will receive the required callbacks from this presenter.
     */
    public FileDownloadPresenterImpl(FileDownloadView fileDownloadView) {
        mFileDownloadView = fileDownloadView;
    }

    @Override
    public void downloadFile(String url) {

        if (mFileDownloadView.checkPermissions()) {
            AsyncFileDownload asyncFileDownload = new AsyncFileDownload(mFileDownloadView, url);
            asyncFileDownload.execute();
        } else {
            mFileDownloadView.askPermissions();
        }
    }

}
