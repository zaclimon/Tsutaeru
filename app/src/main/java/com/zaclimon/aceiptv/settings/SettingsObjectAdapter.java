package com.zaclimon.aceiptv.settings;

import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.presenter.CardViewPresenter;

/**
 * Created by isaac on 17-06-21.
 */

public class SettingsObjectAdapter extends ArrayObjectAdapter {

    public static final String BUNDLE_SETTINGS_NAME_ID = "bundle_name_id";
    public static final String BUNDLE_SETTINGS_DRAWABLE_ID = "bundle_drawable_id";

    public SettingsObjectAdapter() {
        super(new CardViewPresenter());
        setStreamTypeSection();
    }

    private void setStreamTypeSection() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SETTINGS_NAME_ID, R.string.stream_type);
        bundle.putInt(BUNDLE_SETTINGS_DRAWABLE_ID, R.drawable.ic_view_stream);
        add(bundle);
    }
}
