package com.zaclimon.aceiptv.setup;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;

import com.zaclimon.aceiptv.R;

/**
 * Activity that is used to configure the A.C.E. IPTV channels upon going to the
 * Live Channels application.
 *
 * @author zaclimon
 * Creation date: 11/06/17
 */

public class AceTvInputSetupActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ace_setup);
    }
}
