package com.zaclimon.tsutaeru.ui.vod;

import android.content.Context;
import android.content.SharedPreferences;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.util.Constants;

/**
 * Fragment responsible for showing Tv Catchup content for a compatible Tsutaeru provider.
 *
 * @author zaclimon
 * Creation date: 01/07/17
 */

public class CatchupTvFragment extends TsutaeruVodTvSectionFragment {

    @Override
    protected String getVodContentApiLink() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");
        int offset = sharedPreferences.getInt(Constants.EPG_OFFSET_PREFERENCE, 0);

        if (offset != 0) {
            return (getString(R.string.ace_catchup_offset_url, offset, username, password));
        } else {
            return (getString(R.string.ace_catchup_url, username, password));
        }
    }

}
