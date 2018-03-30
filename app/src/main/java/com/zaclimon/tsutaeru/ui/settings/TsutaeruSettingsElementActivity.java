package com.zaclimon.tsutaeru.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v4.app.FragmentActivity;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.ui.auth.AuthActivityTv;
import com.zaclimon.tsutaeru.util.ActivityUtil;
import com.zaclimon.xipl.ui.settings.ProviderSettingsObjectAdapter;

/**
 * Activity that shows a given setting based on it's name
 *
 * @author zaclimon
 * Creation date: 21/06/17
 */

public class TsutaeruSettingsElementActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityUtil.isTvMode(this)) {
            setTheme(R.style.TvTheme);
        }

        Bundle extras = getIntent().getExtras();

        if (savedInstanceState == null && extras != null) {
            // Since the extras contain the id's, we shouldn't have any problems comparing them.
            int stringId = extras.getInt(ProviderSettingsObjectAdapter.BUNDLE_SETTINGS_NAME_ID);

            switch (stringId) {
                case R.string.stream_type:
                    GuidedStepSupportFragment.addAsRoot(this, new StreamTypeGuidedFragment(), android.R.id.content);
                    break;
                case R.string.channel_logo_title:
                case R.string.channel_logo_title_short:
                    GuidedStepSupportFragment.addAsRoot(this, new ChannelLogoGuidedFragment(), android.R.id.content);
                    break;
                case R.string.force_epg_sync_title:
                case R.string.force_epg_sync_title_short:
                    GuidedStepSupportFragment.addAsRoot(this, new EpgForceSyncGuidedFragment(), android.R.id.content);
                    break;
                case R.string.force_video_fit_title:
                case R.string.force_video_fit_title_short:
                    GuidedStepSupportFragment.addAsRoot(this, new ForceFitScreenGuidedFragment(), android.R.id.content);
                    break;
                case R.string.channel_region_title:
                    GuidedStepSupportFragment.addAsRoot(this, new ChannelRegionGuidedFragment(), android.R.id.content);
                    break;
                case R.string.channel_genre_title:
                    GuidedStepSupportFragment.addAsRoot(this, new ChannelGenreGuidedFragment(), android.R.id.content);
                    break;
                case R.string.epg_offset_title:
                case R.string.epg_offset_title_short:
                    GuidedStepSupportFragment.addAsRoot(this, new TvCatchupEpgOffsetGuidedFragment(), android.R.id.content);
                    break;
                case R.string.external_player_title:
                case R.string.external_player_title_short:
                    GuidedStepSupportFragment.addAsRoot(this, new VodExternalPlayerGuidedFragment(), android.R.id.content);
                    break;
                case R.string.change_user_title:
                    Intent intent = new Intent(this, AuthActivityTv.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.string.clear_cache_title:
                case R.string.clear_cache_title_short:
                    GuidedStepSupportFragment.addAsRoot(this, new ClearCacheGuidedFragment(), android.R.id.content);
                    break;
                case R.string.debug_log_title:
                case R.string.debug_log_title_short:
                    GuidedStepSupportFragment.addAsRoot(this, new DebugLogGuidedFragment(), android.R.id.content);
                    break;
                case R.string.about_text:
                    GuidedStepSupportFragment.addAsRoot(this, new AboutGuidedFragment(), android.R.id.content);
                    break;
            }
        }
    }

}
