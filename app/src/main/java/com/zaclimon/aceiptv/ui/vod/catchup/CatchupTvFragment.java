package com.zaclimon.aceiptv.ui.vod.catchup;

import android.content.Context;
import android.content.SharedPreferences;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.ui.vod.VodTvSectionFragment;
import com.zaclimon.aceiptv.util.Constants;

/**
 * Fragment responsible for showing Tv Catchup content for ACE IPTV
 *
 * @author zaclimon
 * Creation date: 01/07/17
 */

public class CatchupTvFragment extends VodTvSectionFragment {

    @Override
    protected String getVodContentApiLink() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ACE_IPTV_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");
        return (getString(R.string.ace_catchup_url, username, password));
    }

}
