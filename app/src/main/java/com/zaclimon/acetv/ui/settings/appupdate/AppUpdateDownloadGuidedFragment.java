package com.zaclimon.acetv.ui.settings.appupdate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentCompat;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.zaclimon.acetv.BuildConfig;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.util.Constants;

import java.io.File;


/**
 * GuidedFragment used to download and prompt the user to update the app. It implements
 * {@link FileDownloadView} to receive callbacks from a {@link FileDownloadPresenter}
 *
 * @author zaclimon
 * Creation date: 04/08/17
 */

public class AppUpdateDownloadGuidedFragment extends GuidedStepFragment implements FileDownloadView {

    protected static final String APP_VERSION_BUNDLE = "app_version";

    private static final String ACE_TV_PACKAGE_FIRST_PART = "AceTV-V";
    private static final String ACE_TV_PACKAGE_EXTENSION = ".apk";
    private static final int STORAGE_PERMISSION_REQUEST = 0;

    @Override
    public void onStart() {
        super.onStart();
        startDownload();
    }

    /**
     * Starts a download for the most recent version of the application.
     */
    private void startDownload() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            String versionName = bundle.getString(APP_VERSION_BUNDLE);
            String url = Constants.ACE_RELEASES_URL + ACE_TV_PACKAGE_FIRST_PART + versionName + ACE_TV_PACKAGE_EXTENSION;

            FileDownloadPresenter fileDownloadPresenter = new FileDownloadPresenterImpl(this);
            fileDownloadPresenter.downloadFile(url);
        }
    }

    @Override
    public GuidanceStylist onCreateGuidanceStylist() {
        return new GuidanceStylist() {
            @Override
            public int onProvideLayoutId() {
                return R.layout.fragment_guided_wizard_loading;
            }
        };
    }

    @Override
    public boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void askPermissions() {
        FragmentCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
    }

    @Override
    public void onDownloadSuccess(String path) {

        Intent installIntent = new Intent();
        Uri uri;

        // Install the update. FileProvider is to be used starting from Nougat.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", new File(path));
        } else {
            uri = Uri.fromFile(new File(path));
        }

        installIntent.setAction(Intent.ACTION_INSTALL_PACKAGE);
        installIntent.setData(uri);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getFragmentManager().popBackStack();
        startActivity(installIntent);

    }

    @Override
    public void onDownloadFailed() {
        Toast.makeText(getActivity(), R.string.app_update_download_failure, Toast.LENGTH_SHORT).show();
        getFragmentManager().popBackStack();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_REQUEST) {

            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            } else {
                Toast.makeText(getActivity(), R.string.write_permissions_denied_toast, Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }

        }
    }

}
