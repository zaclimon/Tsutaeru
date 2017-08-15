package com.zaclimon.acetv.ui.playback;

import android.content.Context;

import com.zaclimon.acetv.R;
import com.zaclimon.acetv.properties.AceVodPlaybackProperties;
import com.zaclimon.acetv.util.Constants;
import com.zaclimon.xipl.properties.VodProperties;
import com.zaclimon.xipl.ui.vod.VodPlaybackFragment;

/**
 * Fragment responsible for playing a given VOD content
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
