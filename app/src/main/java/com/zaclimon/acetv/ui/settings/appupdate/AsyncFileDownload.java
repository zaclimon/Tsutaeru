package com.zaclimon.acetv.ui.settings.appupdate;

import android.os.AsyncTask;
import android.os.Environment;

import com.crashlytics.android.Crashlytics;
import com.zaclimon.acetv.ui.settings.appupdate.FileDownloadView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * AsyncTask responsible for downloading a file based on a given Url.
 *
 * @author zaclimon
 * Creation date: 04/08/17
 */

public class AsyncFileDownload extends AsyncTask<Void, Void, String> {

    private String mDownloadUrl;
    private FileDownloadView mFileDownloadView;

    public AsyncFileDownload(FileDownloadView fileDownloadView, String downloadUrl) {
        mFileDownloadView = fileDownloadView;
        mDownloadUrl = downloadUrl;
    }

    @Override
    public String doInBackground(Void... params) {

        final String fileName = mDownloadUrl.substring(mDownloadUrl.lastIndexOf("/"));
        final File outputFile = new File(Environment.getExternalStorageDirectory() + "/" + fileName);

        try {

             /*
             Delete the file if it existed before. We shouldn't worry about the permissions since
             we asked them in FileDownloadPresenter.
             */

            if (outputFile.isFile()) {
                outputFile.delete();
            }

            URL url = new URL(mDownloadUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();

            InputStream inputStream = new BufferedInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

            byte[] fileData = new byte[1024];
            int count;

            while ((count = inputStream.read(fileData)) != -1) {
                fileOutputStream.write(fileData, 0, count);
            }

            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();

            return (outputFile.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        return (null);
    }

    @Override
    public void onPostExecute(String result) {
        if (result != null) {
            mFileDownloadView.onDownloadSuccess(result);
        } else {
            mFileDownloadView.onDownloadFailed();
        }
    }

}
