package com.zaclimon.tsutaeru.ui.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.service.TsutaeruJobService;
import com.zaclimon.tsutaeru.util.Constants;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * {@link GuidedStepSupportFragment} which lists several regions in which the user can select for his/her
 * Live TV experience.
 *
 * @author zaclimon
 * Creation date: 06/08/17
 */

public class ChannelRegionGuidedFragment extends GuidedStepSupportFragment {

    private final int ACTION_UK_REGION_ID = 0;
    private final int ACTION_NA_REGION_ID = 1;
    private final int ACTION_INT_REGION_ID = 2;

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.channel_region_title);
        String description = getString(R.string.channel_region_description);

        return (new GuidanceStylist.Guidance(title, description, null, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE);

        GuidedAction.Builder ukAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder naAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder intAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder okAction = new GuidedAction.Builder(getActivity());

        // Set the titles
        ukAction.title(R.string.region_uk);
        naAction.title(R.string.region_na);
        intAction.title(R.string.region_international);

        // Set the id's
        ukAction.id(ACTION_UK_REGION_ID);
        naAction.id(ACTION_NA_REGION_ID);
        intAction.id(ACTION_INT_REGION_ID);

        // Set the items to have checkboxes
        ukAction.checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID);
        naAction.checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID);
        intAction.checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID);

        // Set if the value is checked. All of them are enabled by default
        ukAction.checked(sharedPreferences.getBoolean(Constants.UK_REGION_PREFERENCE, true));
        naAction.checked(sharedPreferences.getBoolean(Constants.NA_REGION_PREFERENCE, true));
        intAction.checked(sharedPreferences.getBoolean(Constants.INTERNATIONAL_REGION_PREFERENCE, true));

        // Set an action to confirm the users selection
        okAction.clickAction(GuidedAction.ACTION_ID_OK);

        // Add the actions to the list
        actions.add(ukAction.build());
        actions.add(naAction.build());
        actions.add(intAction.build());
        actions.add(okAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {
        if (guidedAction.getId() == GuidedAction.ACTION_ID_OK) {

            SharedPreferences sharedPrerefences = getActivity().getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrerefences.edit();

            boolean isUkEnabled = sharedPrerefences.getBoolean(Constants.UK_REGION_PREFERENCE, true);
            boolean isNaEnabled = sharedPrerefences.getBoolean(Constants.NA_REGION_PREFERENCE, true);
            boolean isIntEnabled = sharedPrerefences.getBoolean(Constants.INTERNATIONAL_REGION_PREFERENCE, true);

             /*
              A statically defined check shouldn't be the thing to do but since we already know the
              number of elements, it should not be that bad.
              */

            if (getActions().get(0).isChecked() != isUkEnabled || getActions().get(1).isChecked() != isNaEnabled || getActions().get(2).isChecked() != isIntEnabled) {
                String inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT);
                EpgSyncJobService.requestImmediateSync(getActivity(), inputId, TimeUnit.HOURS.toMillis(48), new ComponentName(getActivity(), TsutaeruJobService.class));
                editor.putBoolean(Constants.UK_REGION_PREFERENCE, getActions().get(0).isChecked());
                editor.putBoolean(Constants.NA_REGION_PREFERENCE, getActions().get(1).isChecked());
                editor.putBoolean(Constants.INTERNATIONAL_REGION_PREFERENCE, getActions().get(2).isChecked());
                editor.apply();
                add(getFragmentManager(), new EpgSyncLoadingGuidedFragment());
            } else {
                getActivity().finish();
            }
        }
    }

}
