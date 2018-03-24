package com.zaclimon.acetv.ui.settings;

import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.widget.Toast;

import com.zaclimon.acetv.R;
import com.zaclimon.acetv.data.RealmAvContent;

import java.util.List;

import io.realm.Realm;

/**
 * Guided Fragment responsible for clearing all cache inside the application.
 *
 * @author zaclimon
 * Creation date: 13/08/17
 */

public class ClearCacheGuidedFragment extends GuidedStepSupportFragment {

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.clear_cache_title);
        String description = getString(R.string.clear_cache_description);

        return (new GuidanceStylist.Guidance(title, description, null, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder yesAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder noAction = new GuidedAction.Builder(getActivity());
        yesAction.clickAction(GuidedAction.ACTION_ID_YES);
        noAction.clickAction(GuidedAction.ACTION_ID_NO);
        actions.add(yesAction.build());
        actions.add(noAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {
        if (guidedAction.getId() == GuidedAction.ACTION_ID_YES) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(RealmAvContent.class).findAll().deleteAllFromRealm();
                }
            });
            realm.close();
            Toast.makeText(getActivity(), R.string.database_cleared_text, Toast.LENGTH_SHORT).show();
        }
        getActivity().finish();
    }

}
