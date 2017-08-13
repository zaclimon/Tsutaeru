package com.zaclimon.acetv.ui.playback;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.zaclimon.acetv.R;
import com.zaclimon.acetv.util.ActivityUtil;
import com.zaclimon.xipl.ui.vod.VodPlaybackActivity;
import com.zaclimon.xipl.ui.vod.VodPlaybackFragment;

/**
 * Activity responsible of playing a given VOD content. It is a concrete implementation of
 * {@link VodPlaybackActivity} for Ace TV.
 *
 * @author zaclimon
 * Creation date: 02/07/17
 */

public class AcePlaybackActivity extends VodPlaybackActivity {

    @Override
    protected int getThemeId(){
        return (R.style.TvTheme);
    }

    @Override
    protected VodPlaybackFragment getVodPlaybackFragment() {
        return (new AcePlaybackFragment());
    }

}
