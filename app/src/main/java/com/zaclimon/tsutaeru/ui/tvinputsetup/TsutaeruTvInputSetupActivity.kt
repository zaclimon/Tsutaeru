package com.zaclimon.tsutaeru.ui.tvinputsetup

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.zaclimon.tsutaeru.R

/**
 * Activity that is used to configure the a provider's channels upon going to the
 * Live Channels application.
 *
 * @author zaclimon
 */
class TsutaeruTvInputSetupActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tsutaeru_tv_input_setup)
    }

}