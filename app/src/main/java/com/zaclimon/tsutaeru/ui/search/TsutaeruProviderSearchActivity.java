package com.zaclimon.tsutaeru.ui.search;

import android.support.v17.leanback.app.SearchSupportFragment;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.xipl.ui.search.ProviderSearchActivity;


/**
 * Concrete implementation of a {@link ProviderSearchActivity} for Tsutaeru.
 *
 * @author zaclimon
 * Creation date: 16/08/17
 */

public class TsutaeruProviderSearchActivity extends ProviderSearchActivity {

    @Override
    protected SearchSupportFragment getSearchFragment() {
        return (new TsutaeruProviderSearchFragment());
    }

    @Override
    protected int getThemeId() {
        return (R.style.TvTheme);
    }
}
