package com.zaclimon.acetv.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Toast;

import com.zaclimon.acetv.R;
import com.zaclimon.acetv.ui.auth.FourthAuthGuidedFragment;
import com.zaclimon.acetv.ui.auth.SecondStepAuthGuidedFragment;
import com.zaclimon.acetv.util.Constants;

import java.util.List;

/**
 * Setting section used for setting up the EPG offset for TV Catchup content
 *
 * @author zaclimon
 * Creation date: 22/08/17
 */

public class TvCatchupEpgOffsetGuidedFragment extends GuidedStepFragment {

    private final int ACTION_OFFSET = 0;

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.epg_offset_title);
        String description = getString(R.string.epg_offset_description);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE);
        int offset = sharedPreferences.getInt(Constants.EPG_OFFSET_PREFERENCE, 0);

        String breadcrumb = getString(R.string.current_status_text, getString(R.string.hours_text, offset));

        return (new GuidanceStylist.Guidance(title, description, breadcrumb, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {

        GuidedAction.Builder passwordAction = new GuidedAction.Builder(getActivity());
        passwordAction.title(R.string.offset_text);
        passwordAction.editTitle("");
        passwordAction.editable(true);
        passwordAction.editInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        passwordAction.id(ACTION_OFFSET);
        actions.add(passwordAction.build());
    }

    @Override
    public long onGuidedActionEditedAndProceed(GuidedAction action) {

        int id = (int) action.getId();

        if (id == ACTION_OFFSET) {

            String offset = action.getEditTitle().toString();

            if (!TextUtils.isEmpty(offset)) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.ACE_TV_PREFERENCES, Context.MODE_PRIVATE).edit();
                editor.putInt(Constants.EPG_OFFSET_PREFERENCE, Integer.parseInt(offset));
                editor.apply();
                Toast.makeText(getActivity(), R.string.clear_cache_toast, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else {
                action.setTitle(getString(R.string.offset_empty));
            }
        }
        return (GuidedAction.ACTION_ID_CURRENT);
    }

}
