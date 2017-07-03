package com.zaclimon.aceiptv.ui.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.PageRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.ui.catchup.CatchupTvFragment;
import com.zaclimon.aceiptv.ui.settings.SettingsElementActivity;
import com.zaclimon.aceiptv.ui.settings.SettingsTvFragment;

/**
 * Main fragment used for the Android TV variant of the application
 *
 * @author zaclimon
 * Creation date: 20/06/17
 *
 */

public class MainTvFragment extends BrowseFragment {

    private static final int VOD_ID = 0;
    private static final int CATCHUP_ID = 1;
    private static final int SETTINGS_ID = 2;

    private ArrayObjectAdapter mRowsAdapter;
    private BackgroundManager mBackgroundManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            setupUI();
            showRows();
            setListeners();
            mBackgroundManager = mBackgroundManager.getInstance(getActivity());
            mBackgroundManager.attach(getActivity().getWindow());
            getMainFragmentRegistry().registerFragment(PageRow.class, new TvFragmentFactory());
            startEntranceTransition();
        }
    }

    /**
     * Sets the user interface (UI) for the Fragment.
     */
    private void setupUI() {
        setTitle(getString(R.string.app_name));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        prepareEntranceTransition();
    }

    /**
     * Shows the different rows of the Fragment.
     */
    private void showRows() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setCatchupRow();
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
        HeaderItem settingsHeader = new HeaderItem(SETTINGS_ID, getString(R.string.settings_text));
        PageRow settingsRow = new PageRow(settingsHeader);
        mRowsAdapter.add(settingsRow);
        //mRowsAdapter.add(new ListRow(settingsHeader, new SettingsObjectAdapter()));
    }

    private void setCatchupRow() {
        HeaderItem catchupHeader = new HeaderItem(CATCHUP_ID, getString(R.string.catchup_title));
        PageRow catchupRow = new PageRow(catchupHeader);
        mRowsAdapter.add(catchupRow);
    }

    /**
     * Private class implementing {@link OnItemViewClickedListener} which reacts for a given
     * click when an item in a given row is selected.
     *
     * @author zaclimon
     * Creation date: 21/06/17
     */
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
    private class TvFragmentFactory extends FragmentFactory {

        @Override
        public Fragment createFragment(Object row) {
            Row tempRow = (Row) row;

            if (tempRow.getId() == VOD_ID) {

            } else if (tempRow.getId() == CATCHUP_ID) {
                return (new CatchupTvFragment());
            } else if (tempRow.getId() == SETTINGS_ID) {
                return (new SettingsTvFragment());
            }
            throw new IllegalArgumentException("Invalid row: " + row);
        }

    }

}
