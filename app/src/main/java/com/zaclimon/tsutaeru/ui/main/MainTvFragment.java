package com.zaclimon.tsutaeru.ui.main;

import android.os.Bundle;
import android.support.v17.leanback.app.RowsSupportFragment;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.ui.search.TsutaeruProviderSearchActivity;
import com.zaclimon.tsutaeru.ui.settings.TsutaeruSettingsFragment;
import com.zaclimon.tsutaeru.ui.vod.CatchupTvFragment;
import com.zaclimon.tsutaeru.ui.vod.SeriesTvFragment;
import com.zaclimon.tsutaeru.ui.vod.VodTvFragment;
import com.zaclimon.xipl.ui.main.ProviderTvFragment;
import com.zaclimon.xipl.ui.search.ProviderSearchActivity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Main fragment used for the Android TV variant of the application.
 *
 * @author zaclimon
 * Creation date: 20/06/17
 */

public class MainTvFragment extends ProviderTvFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBadgeDrawable(getActivity().getDrawable(R.drawable.ace_badge_white));
    }

    @Override
    protected String getAppName() {
        return (getString(R.string.app_name));
    }

    @Override
    protected Map<String, RowsSupportFragment> getFragmentMap() {
        // A LinkedHashMap is used in this case since it retains the position of each inserted object.
        Map<String, RowsSupportFragment> sections = new LinkedHashMap<>();

        sections.put(getString(R.string.vod_text), new VodTvFragment());
        sections.put(getString(R.string.series_title), new SeriesTvFragment());
        sections.put(getString(R.string.catchup_title), new CatchupTvFragment());
        sections.put(getString(R.string.settings_text), new TsutaeruSettingsFragment());
        return (sections);
    }

    @Override
    protected Class<? extends ProviderSearchActivity> getSearchActivity() {
        return (TsutaeruProviderSearchActivity.class);
    }

}
