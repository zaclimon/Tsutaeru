package com.zaclimon.acetv.ui.vod;

import android.content.Context;
import android.content.SharedPreferences;

import com.zaclimon.acetv.R;
import com.zaclimon.acetv.util.Constants;

/**
 * Fragment responsible for showing TV shows for Ace TV.
 *
 * @author zaclimon
 * Creation date: 05/07/17
 */

public class SeriesTvFragment extends AceVodTvSectionFragment {

    @Override
    protected String getVodContentApiLink() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");
        return (getString(R.string.ace_series_url, username, password));
    }

}
