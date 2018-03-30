package com.zaclimon.tsutaeru.ui.vod;

import android.content.Context;
import android.content.SharedPreferences;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.util.Constants;

/**
 * Fragment responsible for showing VOD content for Tsutaeru providers.
 *
 * @author zaclimon
 * Creation date: 05/07/17
 */

public class VodTvFragment extends TsutaeruVodTvSectionFragment {

    @Override
    protected String getVodContentApiLink() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");
        return (getString(R.string.ace_vod_url, username, password));
    }

}
