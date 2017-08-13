package com.zaclimon.acetv.ui.playback;

import android.app.FragmentTransaction;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v17.leanback.app.ErrorFragment;
import android.support.v17.leanback.app.VideoFragment;
import android.support.v17.leanback.app.VideoFragmentGlueHost;
import android.support.v17.leanback.media.PlaybackGlue;
import android.support.v17.leanback.media.PlaybackTransportControlGlue;
import android.util.DisplayMetrics;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.ui.vod.VodTvSectionFragment;
import com.zaclimon.acetv.util.ActivityUtil;
import com.zaclimon.acetv.util.Constants;
import com.zaclimon.xipl.properties.VodProperties;
import com.zaclimon.xipl.ui.vod.ExoPlayerAdapter;
import com.zaclimon.xipl.ui.vod.VodPlaybackFragment;

/**
 * Fragment responsible for playing an {@link com.zaclimon.acetv.data.AvContent}
 *
 * @author zaclimon
 * Creation date: 02/07/17
 */

public class AcePlaybackFragment extends VodPlaybackFragment {

    @Override
    public VodProperties getVodProperties() {
        return (new AceVodPlaybackProperties(getActivity().getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE)));
    }

    @Override
    public String getProviderName() {
        return (getString(R.string.app_name));
    }

}
