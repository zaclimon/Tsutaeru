package com.zaclimon.acetv.ui.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.widget.Toast;

import com.zaclimon.acetv.R;
import com.zaclimon.acetv.ui.settings.AceSettingsElementActivity;
import com.zaclimon.acetv.ui.settings.AceSettingsFragment;
import com.zaclimon.acetv.ui.settings.appupdate.AppUpdatePresenterImpl;
import com.zaclimon.acetv.ui.settings.appupdate.AppUpdateView;
import com.zaclimon.acetv.ui.vod.CatchupTvFragment;
import com.zaclimon.acetv.ui.vod.SeriesTvFragment;
import com.zaclimon.acetv.ui.vod.VodTvFragment;
import com.zaclimon.xipl.ui.main.ProviderTvFragment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Main fragment used for the Android TV variant of the application.
 *
 * @author zaclimon
 * Creation date: 20/06/17
 */

public class MainTvFragment extends ProviderTvFragment implements AppUpdateView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBadgeDrawable(getActivity().getDrawable(R.drawable.ace_badge_white));
        new AppUpdatePresenterImpl(this).fetchUpdate();
    }

    @Override
    protected String getAppName() {
        return (getString(R.string.app_name));
    }

    @Override
    protected Map<String, RowsFragment> getFragmentMap() {
        // A LinkedHashMap is used in this case since it retains the position of each inserted object.
        Map<String, RowsFragment> aceFragments = new LinkedHashMap<>();

        aceFragments.put(getString(R.string.vod_text), new VodTvFragment());
        aceFragments.put(getString(R.string.series_title), new SeriesTvFragment());
        aceFragments.put(getString(R.string.catchup_title), new CatchupTvFragment());
        aceFragments.put(getString(R.string.settings_text), new AceSettingsFragment());
        return (aceFragments);
    }

    @Override
    public void onNewVersionDetected(String versionName, String[] changelog) {
        Toast.makeText(getActivity(), getString(R.string.new_version_available_toast, versionName), Toast.LENGTH_SHORT).show();
    }

    // No need to implement these methods
    @Override
    public void onSameVersionDetected() {
    }

    @Override
    public void onFetchFailed() {
    }

}
