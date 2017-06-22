package com.zaclimon.aceiptv.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.settings.SettingsElementActivity;
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
        setListeners();
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

    private void setListeners() {
        setOnItemViewClickedListener(new TvItemClickListener());
    }

    private void setSettingsRow() {
        HeaderItem settingsHeader = new HeaderItem(getString(R.string.settings_text));
        mRowsAdapter.add(new ListRow(settingsHeader, new SettingsObjectAdapter()));
    }

    private class TvItemClickListener implements OnItemViewClickedListener {

        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Bundle) {
                // The item comes from a Settings element.
                Bundle bundle = (Bundle) item;
                Intent intent = new Intent(itemViewHolder.view.getContext(), SettingsElementActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }

    }

}
