package com.zaclimon.tsutaeru.ui.settings.userinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v17.leanback.app.ErrorSupportFragment;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.app.ProgressBarManager;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaclimon.tsutaeru.R;
import com.zaclimon.tsutaeru.util.Constants;

import java.util.Date;
import java.util.List;

/**
 * Fragment used to show the current user's account status.
 *
 * @author zaclimon
 * Creation date: 03/07/17
 */

public class UserInfoGuidedFragment extends GuidedStepSupportFragment implements UserInfoView {

    private ProgressBarManager mProgressBarManager;

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

        UserInfoPresenter userInfoPresenter = new UserInfoPresenterImpl(this);
        userInfoPresenter.retrieveUserInfo();
    }

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE);
        String title = getString(R.string.user_info_text);
        String breadcrumb = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");

        return (new GuidanceStylist.Guidance(title, null, breadcrumb, null));
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {

        GuidedAction.Builder builder = new GuidedAction.Builder(getActivity());
        builder.clickAction(GuidedAction.ACTION_ID_OK);
        actions.add(builder.build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction guidedAction) {

         /*
          There seems to have an issue with popBackStackToGuidedStepFragment(), let's just use the
          classical popBackStack().
          */

        getFragmentManager().popBackStack();

    }

    @Override
    public void onConnectionSuccess(String status, Date expirationDate, boolean isTrial, int maxConnections) {

        if (isAdded()) {
            TextView descriptionView = getGuidanceStylist().getDescriptionView();
            String trial = isTrial ? getString(R.string.yes_text) : getString(R.string.no_text);

            mProgressBarManager.hide();

            getGuidanceStylist().getTitleView().setVisibility(View.VISIBLE);
            descriptionView.setText(getString(R.string.user_info_description, status, expirationDate, trial, Integer.toString(maxConnections)));
            descriptionView.setVisibility(View.VISIBLE);
            getGuidanceStylist().getBreadcrumbView().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnectionFailed() {
        if (isAdded()) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            ErrorSupportFragment errorFragment = new ErrorSupportFragment();
            errorFragment.setImageDrawable(getActivity().getDrawable(R.drawable.lb_ic_sad_cloud));
            errorFragment.setMessage(getString(R.string.user_info_not_accessible));
            errorFragment.setDefaultBackground(true);
            transaction.add(android.R.id.content, errorFragment);
            mProgressBarManager.hide();
            transaction.commit();
        }
    }

    @Override
    public String getUserInfoApiEndpoint() {
        if (isAdded()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.TSUTAERU_PREFERENCES, Context.MODE_PRIVATE);
            String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
            String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");
            return (getString(R.string.ace_user_info_url, username, password));
        } else {
            return (null);
        }
    }
}
