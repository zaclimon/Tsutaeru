package com.zaclimon.tsutaeru.ui.playback;

import android.content.Context;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.properties.TsutaeruVodPlaybackProperties;
import com.zaclimon.tsutaeru.util.Constants;
import com.zaclimon.xipl.properties.VodProperties;
import com.zaclimon.xipl.ui.vod.VodPlaybackFragment;

/**
 * Fragment responsible for playing a given VOD content.
 *
 * @author zaclimon
 * Creation date: 02/07/17
 */

public class TsutaeruPlaybackFragment extends VodPlaybackFragment {

    @Override
    public VodProperties getVodProperties() {
        return (new TsutaeruVodPlaybackProperties(getActivity().getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE)));
    }

    @Override
    public String getProviderName() {
        return (getString(R.string.app_name));
    }

}
