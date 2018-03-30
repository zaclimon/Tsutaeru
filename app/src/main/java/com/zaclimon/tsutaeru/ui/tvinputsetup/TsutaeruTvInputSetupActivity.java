package com.zaclimon.tsutaeru.ui.tvinputsetup;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zaclimon.tsutaeru.R;

/**
 * Activity that is used to configure the a provider's channels upon going to the
 * Live Channels application.
 *
 * @author zaclimon
 * Creation date: 11/06/17
 */

public class TsutaeruTvInputSetupActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tsutaeru_tv_input_setup);
    }
}
