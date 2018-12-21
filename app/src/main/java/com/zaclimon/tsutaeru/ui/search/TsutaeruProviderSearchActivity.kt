package com.zaclimon.tsutaeru.ui.search

import androidx.leanback.app.SearchSupportFragment
import com.zaclimon.tsutaeru.R
import com.zaclimon.xipl.ui.search.ProviderSearchActivity

/**
 * Concrete implementation of a [ProviderSearchActivity] for Tsutaeru.
 *
 * @author zaclimon
 */
class TsutaeruProviderSearchActivity : ProviderSearchActivity() {

    override fun getSearchFragment(): SearchSupportFragment {
        return TsutaeruProviderSearchFragment()
    }

    override fun getThemeId(): Int {
        return R.style.TvTheme
    }

}