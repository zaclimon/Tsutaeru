package com.zaclimon.aceiptv.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.settings.channellogo.ChannelLogoGuidedFragment;
import com.zaclimon.aceiptv.settings.epgforcesync.EpgForceSyncGuidedFragment;
import com.zaclimon.aceiptv.settings.streamtype.StreamTypeGuidedFragment;

/**
 * Created by isaac on 17-06-21.
 */

public class SettingsElementActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if (savedInstanceState == null && extras != null) {
            // Since the extras contain the id's, we shouldn't have any problems comparing them.
            int stringId = extras.getInt(SettingsObjectAdapter.BUNDLE_SETTINGS_NAME_ID);

            switch (stringId) {
                case R.string.stream_type:
                    GuidedStepFragment.addAsRoot(this, new StreamTypeGuidedFragment(), android.R.id.content);
                    break;
                case R.string.channel_logo_title:
                case R.string.channel_logo_title_short:
                    GuidedStepFragment.addAsRoot(this, new ChannelLogoGuidedFragment(), android.R.id.content);
                    break;
                case R.string.force_epg_sync_title:
                case R.string.force_epg_sync_title_short:
                    GuidedStepFragment.addAsRoot(this, new EpgForceSyncGuidedFragment(), android.R.id.content);
                    break;
            }
        }
    }

}
