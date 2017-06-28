package com.zaclimon.aceiptv.settings;

import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.presenter.CardViewPresenter;

/**
 * Class extending {@link ArrayObjectAdapter} which gives a list of current
 * settings elements.
 *
 * It's main view is a {@link CardViewPresenter}
 *
 * @author zaclimon
 * Creation date: 21/06/17
 */

public class SettingsObjectAdapter extends ArrayObjectAdapter {

    /**
     * Static variable for identifying the name id.
     */
    public static final String BUNDLE_SETTINGS_NAME_ID = "bundle_name_id";

    /**
     * Static variable for identifying the drawable id.
     */
    public static final String BUNDLE_SETTINGS_DRAWABLE_ID = "bundle_drawable_id";

    /**
     * Default constructor. Sets up all settings elements
     */
    public SettingsObjectAdapter() {
        super(new CardViewPresenter());
        setStreamTypeSection();
        setChannelLogoSection();
        setForceEpgSyncSection();
        setChangeUserSection();
        setDebugLogSection();
    }

    /**
     * Configures the Stream type section
     */
    private void setStreamTypeSection() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SETTINGS_NAME_ID, R.string.stream_type);
        bundle.putInt(BUNDLE_SETTINGS_DRAWABLE_ID, R.drawable.ic_view_stream);
        add(bundle);
    }

    /**
     * Configures the Channel Logo section for the Live Channels application.
     */
    private void setChannelLogoSection() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SETTINGS_NAME_ID, R.string.channel_logo_title_short);
        bundle.putInt(BUNDLE_SETTINGS_DRAWABLE_ID, R.drawable.ic_channel_logo);
        add(bundle);
    }

    /**
     * Configures the EPG sync forcing section.
     */
    private void setForceEpgSyncSection() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SETTINGS_NAME_ID, R.string.force_epg_sync_title_short);
        bundle.putInt(BUNDLE_SETTINGS_DRAWABLE_ID, R.drawable.ic_sync_epg);
        add(bundle);
    }

    /**
     * Configures the user switch section.
     */
    private void setChangeUserSection() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SETTINGS_NAME_ID, R.string.change_user_title);
        bundle.putInt(BUNDLE_SETTINGS_DRAWABLE_ID, R.drawable.ic_change_user);
        add(bundle);
    }

    private void setDebugLogSection() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SETTINGS_NAME_ID, R.string.debug_log_title_short);
        bundle.putInt(BUNDLE_SETTINGS_DRAWABLE_ID, R.drawable.ic_debug);
        add(bundle);
    }
}
