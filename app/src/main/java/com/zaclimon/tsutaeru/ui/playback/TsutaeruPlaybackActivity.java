package com.zaclimon.tsutaeru.ui.playback;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.xipl.ui.vod.VodPlaybackActivity;
import com.zaclimon.xipl.ui.vod.VodPlaybackFragment;

/**
 * Activity responsible of playing a given VOD content. It is a concrete implementation of
 * {@link VodPlaybackActivity} for Tsutaeru.
 *
 * @author zaclimon
 * Creation date: 02/07/17
 */

public class TsutaeruPlaybackActivity extends VodPlaybackActivity {

    @Override
    protected int getThemeId(){
        return (R.style.TvTheme);
    }

    @Override
    protected VodPlaybackFragment getVodPlaybackFragment() {
        return (new TsutaeruPlaybackFragment());
    }

}
