package com.zaclimon.acetv.ui.search;

import android.support.v17.leanback.app.SearchSupportFragment;

import com.zaclimon.acetv.R;
import com.zaclimon.xipl.ui.search.ProviderSearchActivity;


/**
 * Concrete implementation of a {@link ProviderSearchActivity} for Ace TV.
 *
 * @author zaclimon
 * Creation date: 16/08/17
 */

public class AceProviderSearchActivity extends ProviderSearchActivity {

    @Override
    protected SearchSupportFragment getSearchFragment() {
        return (new AceProviderSearchFragment());
    }

    @Override
    protected int getThemeId() {
        return (R.style.TvTheme);
    }
}
