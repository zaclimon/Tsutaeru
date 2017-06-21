package com.zaclimon.aceiptv.main;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.settings.SettingsObjectAdapter;

/**
 * Created by isaac on 17-06-20.
 */

public class MainTvFragment extends BrowseFragment {

    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUI();
        showRows();
    }

    private void setupUI() {
        setTitle(getString(R.string.app_name));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
    }

    private void showRows() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setSettingsRow();
        setAdapter(mRowsAdapter);
    }

    private void setSettingsRow() {
        HeaderItem settingsHeader = new HeaderItem(getString(R.string.settings_text));
        mRowsAdapter.add(new ListRow(settingsHeader, new SettingsObjectAdapter()));
    }
}
