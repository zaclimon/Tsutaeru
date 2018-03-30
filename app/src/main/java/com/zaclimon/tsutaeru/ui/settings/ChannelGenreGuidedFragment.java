package com.zaclimon.tsutaeru.ui.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import com.google.android.media.tv.companionlibrary.EpgSyncJobService;
import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.service.TsutaeruJobService;
import com.zaclimon.tsutaeru.util.Constants;

import java.util.List;

/**
 * Settings option that lets a user filter the channels he/she wants to watch based on
 * {@link android.media.tv.TvContract.Programs.Genres}. The channels are classified on a best
 * effort basis.
 *
 * @author zaclimon
 * Creation date: 28/04/18
 */
public class ChannelGenreGuidedFragment extends GuidedStepSupportFragment {

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        String title = getString(R.string.channel_genre_title);
        String description = getString(R.string.channel_genre_description);

        return (new GuidanceStylist.Guidance(title, description, null, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE);
        String[] channelGenres = Constants.CHANNEL_GENRES;

        for (int i = 0; i < channelGenres.length; i++) {
            GuidedAction.Builder action = new GuidedAction.Builder(getContext());
            // Create a title with a first capital letter. Also replace the "_" by a space
            String actionTitle = channelGenres[i].substring(0, 1).toUpperCase() + channelGenres[i].substring(1).toLowerCase();
            actionTitle = actionTitle.replace("_", " ");
            action.id(i);
            action.checkSetId(GuidedAction.CHECKBOX_CHECK_SET_ID);
            action.title(actionTitle);
            action.checked(sharedPreferences.getBoolean(Constants.CHANNEL_GENRE_PREFERENCE + channelGenres[i], true));
            actions.add(action.build());
        }

        GuidedAction.Builder okAction = new GuidedAction.Builder(getContext());
        GuidedAction.Builder cancelAction = new GuidedAction.Builder(getContext());
        okAction.clickAction(GuidedAction.ACTION_ID_OK);
        cancelAction.clickAction(GuidedAction.ACTION_ID_CANCEL);
        actions.add(okAction.build());
        actions.add(cancelAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {
        if (guidedAction.getId() == GuidedAction.ACTION_ID_OK) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            List<GuidedAction> actions = getActions();
            String[] channelGenres = Constants.CHANNEL_GENRES;
            boolean hasAnyPreferenceChanged = false;
            // Size of all the actions without "ok" and "cancel"
            int checkboxActionsSize = actions.size() - 2;

            for (int i = 0; i < checkboxActionsSize; i++) {
                boolean currentPreference = sharedPreferences.getBoolean(Constants.CHANNEL_GENRE_PREFERENCE + channelGenres[i], true);
                if (currentPreference != actions.get(i).isChecked()) {
                    if (!hasAnyPreferenceChanged) {
                        hasAnyPreferenceChanged = true;
                    }
                    editor.putBoolean(Constants.CHANNEL_GENRE_PREFERENCE + channelGenres[i], actions.get(i).isChecked());
                }
            }

            if (hasAnyPreferenceChanged) {
                editor.apply();
                String inputId = TvContract.buildInputId(Constants.TV_INPUT_SERVICE_COMPONENT);
                EpgSyncJobService.requestImmediateSync(getActivity(), inputId, new ComponentName(getActivity(), TsutaeruJobService.class));
                add(getFragmentManager(), new EpgSyncLoadingGuidedFragment());
            } else {
                getActivity().finish();
            }
        } else if (guidedAction.getId() == GuidedAction.ACTION_ID_CANCEL) {
            getActivity().finish();
        }
    }

}
