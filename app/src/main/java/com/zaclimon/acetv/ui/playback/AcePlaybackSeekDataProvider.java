package com.zaclimon.acetv.ui.playback;

import android.support.v17.leanback.widget.PlaybackSeekDataProvider;

/**
 * Created by isaac on 17-07-31.
 */

public class AcePlaybackSeekDataProvider extends PlaybackSeekDataProvider {

    // 10 seconds between each seeking steps.
    private static final int SEEK_STEPS_DURATION_MILLIS = 10000;

    private long[] durations;

    public AcePlaybackSeekDataProvider(long duration) {
        init(duration);
    }

    private void init(long duration) {
        int steps = (int) (duration / SEEK_STEPS_DURATION_MILLIS);
        durations = new long[steps];

        int currentMillis = 0;

        for (int i = 0; i < durations.length; i++) {
            durations[i] = currentMillis;
            currentMillis += SEEK_STEPS_DURATION_MILLIS;
        }
    }

    @Override
    public long[] getSeekPositions() {
        return (durations);
    }

}
