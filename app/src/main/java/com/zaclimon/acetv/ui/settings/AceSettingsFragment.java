package com.zaclimon.acetv.ui.settings;

import android.app.Activity;
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

import com.zaclimon.xipl.ui.settings.ProviderSettingsTvFragment;

/**
 * Fragment handling the Settings from a TV standpoint. Concrete {@link ProviderSettingsTvFragment}
 * for Ace TV.
 *
 * @author zaclimon
 * Creation date: 01/07/17
 */

public class AceSettingsFragment extends ProviderSettingsTvFragment {

    @Override
    public Class<? extends Activity> getSettingsElementActivity() {
        return (AceSettingsElementActivity.class);
    }

    @Override
    public ArrayObjectAdapter getSettingsObjectAdapter() {
        return (new AceSettingsObjectAdapter());
    }

}
