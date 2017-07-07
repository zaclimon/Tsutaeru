package com.zaclimon.acetv.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.RowsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

/**
 * Fragment handling the Settings from a TV standpoint
 *
 * @author zaclimon
 * Creation date: 01/07/17
 */

public class SettingsTvFragment extends RowsFragment {

    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        mRowsAdapter.add(new ListRow(new SettingsObjectAdapter()));

        setAdapter(mRowsAdapter);
        setOnItemViewClickedListener(new SettingsItemClickListener());

        getMainFragmentAdapter().getFragmentHost().notifyDataReady(getMainFragmentAdapter());
    }

    /**
     * Class offering an {@link OnItemViewClickedListener} for the various settings elements.
     *
     * @author zaclimon
     * Creation date: 01/07/17
     */
    private class SettingsItemClickListener implements OnItemViewClickedListener {

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
