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
 * Main fragment used for the Android TV variant of the application
 *
 * @author zaclimon
 * Creation date: 20/06/17
 *
 */

public class MainTvFragment extends BrowseFragment {

    private ArrayObjectAdapter mRowsAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUI();
        showRows();
        setListeners();
    }

    /**
     * Sets the user interface (UI) for the Fragment.
     */
    private void setupUI() {
        setTitle(getString(R.string.app_name));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
    }

    /**
     * Shows the different rows of the Fragment.
     */
    private void showRows() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setSettingsRow();
        setAdapter(mRowsAdapter);
    }

    /**
     * Configures the listeners for the different rows
     */
    private void setListeners() {
        setOnItemViewClickedListener(new TvItemClickListener());
    }

    /**
     * Configures the settings row which will be used as a Settings section.
     */
    private void setSettingsRow() {
        HeaderItem settingsHeader = new HeaderItem(getString(R.string.settings_text));
        mRowsAdapter.add(new ListRow(settingsHeader, new SettingsObjectAdapter()));
    }

    /**
     * Private class implementing {@link OnItemViewClickedListener} which reacts for a given
     * click when an item in a given row is selected.
     *
     * @author zaclimon
     * Creation date: 21/06/17
     */
    private class TvItemClickListener implements OnItemViewClickedListener {

        /**
         * {@inheritDoc}
         */
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
