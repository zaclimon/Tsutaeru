package com.zaclimon.aceiptv.playback;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.zaclimon.aceiptv.R;

/**
 * Created by isaac on 17-07-02.
 */

public class PlaybackActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment playbackFragment = new PlaybackFragment();
        Bundle arguments = getIntent().getExtras();
        playbackFragment.setArguments(arguments);
        fragmentTransaction.add(R.id.activity_playback_fragment, playbackFragment);

        fragmentTransaction.commit();

    }
}
