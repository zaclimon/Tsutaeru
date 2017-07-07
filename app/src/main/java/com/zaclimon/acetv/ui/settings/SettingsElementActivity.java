package com.zaclimon.acetv.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.zaclimon.acetv.R;
import com.zaclimon.acetv.ui.auth.AuthActivityTv;

/**
 * Activity that shows a given setting based on it's name
 *
 * @author zaclimon
 * Creation date: 21/06/17
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
                case R.string.change_user_title:
                    Intent intent = new Intent(this, AuthActivityTv.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.string.debug_log_title:
                case R.string.debug_log_title_short:
                    GuidedStepFragment.addAsRoot(this, new DebugLogGuidedFragment(), android.R.id.content);
                    break;
                case R.string.about_text:
                    GuidedStepFragment.addAsRoot(this, new AboutGuidedFragment(), android.R.id.content);
                    break;
            }
        }
    }

}
