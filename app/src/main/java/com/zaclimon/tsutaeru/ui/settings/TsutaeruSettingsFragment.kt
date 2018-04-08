package com.zaclimon.tsutaeru.ui.settings

import android.app.Activity
import android.support.v17.leanback.widget.ArrayObjectAdapter
import com.zaclimon.xipl.ui.settings.ProviderSettingsTvFragment

/**
 * Fragment handling the Settings from a TV standpoint. Concrete implementation of a
 * [ProviderSettingsTvFragment] for Tsutaeru.
 *
 * @author zaclimon
 */
class TsutaeruSettingsFragment : ProviderSettingsTvFragment() {

    override fun getSettingsElementActivity(): Class<out Activity> {
        return (TsutaeruSettingsElementActivity::class.java)
    }

    override fun getSettingsObjectAdapter(): ArrayObjectAdapter {
        return (TsutaeruSettingsObjectAdapter())
    }

}