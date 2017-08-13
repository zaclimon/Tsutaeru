package com.zaclimon.acetv.ui.settings;

import com.zaclimon.acetv.R;
import com.zaclimon.xipl.ui.settings.ProviderSettingsObjectAdapter;

/**
 * Implementation of {@link ProviderSettingsObjectAdapter} for Ace TV.
 *
 * @author zaclimon
 * Creation date: 21/06/17
 */

public class AceSettingsObjectAdapter extends ProviderSettingsObjectAdapter {

    public AceSettingsObjectAdapter() {
        addSection(R.string.stream_type, R.drawable.ic_view_stream);
        addSection(R.string.channel_logo_title_short, R.drawable.ic_channel_logo);
        addSection(R.string.force_epg_sync_title_short, R.drawable.ic_sync_epg);
        addSection(R.string.force_video_fit_title_short, R.drawable.ic_force_video_fit);
        addSection(R.string.channel_region_title, R.drawable.ic_channel_region);
        addSection(R.string.change_user_title, R.drawable.ic_change_user);
        addSection(R.string.clear_cache_title_short, R.drawable.ic_clear_cache);
        addSection(R.string.debug_log_title_short, R.drawable.ic_debug);
        addSection(R.string.about_text, R.drawable.ic_about);
    }
}
