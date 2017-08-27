package com.zaclimon.acetv.ui.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v13.app.FragmentCompat;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.zaclimon.acetv.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Setting section where the users can create a log file that can be sent over for debugging
 * purposes.
 * <p>
 * The logs are generated using Logcat. Note that since Android 4.1, applications can only see
 * the debugging information related to their process. If one wants to see a device's activity,
 * he/she needs to use adb for further needs.
 *
 * @author zaclimon
 * Creation date: 28/06/17
 */

public class DebugLogGuidedFragment extends GuidedStepFragment {

    private final int WRITE_PERMISSION_REQUEST = 0;

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.debug_log_title);
        String description = getString(R.string.debug_log_description);

        return (new GuidanceStylist.Guidance(title, description, null, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder yesAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder noAction = new GuidedAction.Builder(getActivity());
        yesAction.clickAction(GuidedAction.ACTION_ID_YES);
        noAction.clickAction(GuidedAction.ACTION_ID_NO);
        actions.add(yesAction.build());
        actions.add(noAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {
        if (guidedAction.getId() == GuidedAction.ACTION_ID_YES) {
            // Let's ask for the write permissions first
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                new AsyncLoadLogcat().execute();
            } else {
                FragmentCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
            }
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new AsyncLoadLogcat().execute();
            } else {
                Toast.makeText(getActivity(), R.string.write_permissions_denied_toast, Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Class that gets the output from the logcat and then saves it in the internal storage of a
     * given device.
     * <p>
     * Made in an asynchronous fashion in order to keep a consistent UI experience.
     *
     * @author zaclimon
     *         Creation date: 28/06/17
     */
    private class AsyncLoadLogcat extends AsyncTask<Void, Void, Boolean> {

        private final String LOG_PATH = Environment.getExternalStorageDirectory() + "/Ace-log-" + new SimpleDateFormat("dd-MM-YYYY-HH:mm:ss", Locale.ENGLISH).format(new Date()) + ".txt";
        private final String LOGCAT_COMMAND = "logcat -d";
        private final int OUTPUT_STREAM_BUFFER = 4096;

        @Override
        public Boolean doInBackground(Void... params) {

            try {
                Process process = Runtime.getRuntime().exec(LOGCAT_COMMAND);
                InputStream inputStream = process.getInputStream();
                byte[] buffer = new byte[OUTPUT_STREAM_BUFFER];
                int bytesRead;

                File targetFile = new File(LOG_PATH);
                OutputStream outputStream = new FileOutputStream(targetFile);

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

            } catch (IOException io) {
                return (false);
            }
            return (true);
        }

        @Override
        public void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(getActivity(), getString(R.string.file_saved_storage, LOG_PATH), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), R.string.file_saving_error, Toast.LENGTH_SHORT).show();
            }

            getActivity().finish();

        }

    }

}
