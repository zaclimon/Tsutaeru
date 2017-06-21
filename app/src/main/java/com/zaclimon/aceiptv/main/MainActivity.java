package com.zaclimon.aceiptv.main;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.UiModeManager;
import android.content.res.Configuration;
import android.os.Bundle;

import com.zaclimon.aceiptv.R;


/**
 * Created by isaac on 17-06-20.
 */

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);

        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            // We're in TV mode, show the TV fragment
            fragmentTransaction.add(R.id.activity_fragment_holder, new MainTvFragment());
        }

        fragmentTransaction.commit();
    }

}
