package com.zaclimon.acetv.ui.tvinputsetup;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zaclimon.acetv.R;

/**
 * Activity that is used to configure the Ace TV channels upon going to the
 * Live Channels application.
 *
 * @author zaclimon
 * Creation date: 11/06/17
 */

public class AceTvInputSetupActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ace_tv_input_setup);
    }
}
