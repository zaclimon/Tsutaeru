package com.zaclimon.acetv.ui.settings.appupdate;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.app.ProgressBarManager;
import android.support.v17.leanback.widget.GuidedAction;
import android.view.View;
import android.view.ViewGroup;

import com.zaclimon.acetv.BuildConfig;
import com.zaclimon.acetv.R;

import java.util.List;

/**
 * Fragment used to determine whether the application needs update or not.
 *
 * @author zaclimon
 * Creation date: 04/08/17
 */

public class AppUpdateGuidedFragment extends GuidedStepFragment implements AppUpdateView {

    private ProgressBarManager mProgressBarManager;
    private String mNewVersionName;
    private final int DOWNLOAD_ID = 0;

    @Override
    public void onStart() {
        super.onStart();

        // Don't show the guidance stuff for the moment and show the progress bar
        getGuidanceStylist().getTitleView().setVisibility(View.INVISIBLE);
        getGuidanceStylist().getDescriptionView().setVisibility(View.INVISIBLE);
        getGuidanceStylist().getBreadcrumbView().setVisibility(View.INVISIBLE);

        mProgressBarManager = new ProgressBarManager();
        mProgressBarManager.setRootView((ViewGroup) getActivity().findViewById(android.R.id.content));
        mProgressBarManager.setInitialDelay(250);
        mProgressBarManager.show();

        AppUpdatePresenter appUpdatePresenter = new AppUpdatePresenterImpl(this);
        appUpdatePresenter.fetchUpdate();
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction.Builder downloadUpdateAction = new GuidedAction.Builder(getActivity());
        GuidedAction.Builder okAction = new GuidedAction.Builder(getActivity());
        downloadUpdateAction.title(R.string.download_update_text);
        okAction.title(R.string.ok_text);
        downloadUpdateAction.id(DOWNLOAD_ID);
        okAction.clickAction(GuidedAction.ACTION_ID_OK);
        downloadUpdateAction.focusable(false);
        downloadUpdateAction.enabled(false);
        actions.add(downloadUpdateAction.build());
        actions.add(okAction.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {

        FragmentManager fragmentManager = getFragmentManager();
        int id = (int) guidedAction.getId();

        if (id == DOWNLOAD_ID) {
            GuidedStepFragment downloadFragment = new AppUpdateDownloadGuidedFragment();
            Bundle bundle = new Bundle();
            bundle.putString(AppUpdateDownloadGuidedFragment.APP_VERSION_BUNDLE, mNewVersionName);
            downloadFragment.setArguments(bundle);

            add(getFragmentManager(), downloadFragment);
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            getActivity().finish();
        }

    }


    @Override
    public void onNewVersionDetected(String versionName, String[] changelog) {

        StringBuilder changelogBuilder = new StringBuilder();
        List<GuidedAction> actions = getActions();

        for (String changelogElement : changelog) {
            // Some little formatting...
            changelogBuilder.append("- ");
            changelogBuilder.append(changelogElement);
            changelogBuilder.append(System.lineSeparator());
        }

        for (int i = 0; i < actions.size(); i++) {
            GuidedAction action = actions.get(i);
            if (action.getId() == DOWNLOAD_ID) {
                action.setFocusable(true);
                action.setEnabled(true);
                notifyActionChanged(i);
            }
        }

        mProgressBarManager.hide();
        getGuidanceStylist().getTitleView().setText(R.string.new_version_available);
        getGuidanceStylist().getBreadcrumbView().setText(getString(R.string.version_text, versionName));
        getGuidanceStylist().getDescriptionView().setText(changelogBuilder.toString());

        getGuidanceStylist().getTitleView().setVisibility(View.VISIBLE);
        getGuidanceStylist().getBreadcrumbView().setVisibility(View.VISIBLE);
        getGuidanceStylist().getDescriptionView().setVisibility(View.VISIBLE);
        mNewVersionName = versionName;

    }

    @Override
    public void onSameVersionDetected() {
        mProgressBarManager.hide();
        getGuidanceStylist().getTitleView().setText(R.string.no_update_available);
        getGuidanceStylist().getBreadcrumbView().setText(getString(R.string.version_text, BuildConfig.VERSION_NAME));
        getGuidanceStylist().getTitleView().setVisibility(View.VISIBLE);
        getGuidanceStylist().getBreadcrumbView().setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchFailed() {
        mProgressBarManager.hide();
        getGuidanceStylist().getTitleView().setText(R.string.error_text);
        getGuidanceStylist().getDescriptionView().setText(R.string.update_check_failed);
        getGuidanceStylist().getTitleView().setVisibility(View.VISIBLE);
        getGuidanceStylist().getDescriptionView().setVisibility(View.VISIBLE);
    }

}
