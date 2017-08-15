package com.zaclimon.acetv.ui.settings;

import android.app.Activity;
import android.support.v17.leanback.widget.ArrayObjectAdapter;

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
