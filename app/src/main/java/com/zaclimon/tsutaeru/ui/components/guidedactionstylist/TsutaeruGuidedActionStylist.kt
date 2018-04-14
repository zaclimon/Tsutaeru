package com.zaclimon.tsutaeru.ui.components.guidedactionstylist

import android.support.v17.leanback.widget.GuidedActionsStylist
import com.zaclimon.tsutaeru.R

class TsutaeruGuidedActionStylist : GuidedActionsStylist() {

    override fun onProvideItemLayoutId(): Int {
        return R.layout.tsutaeru_guidedactions_item
    }

}