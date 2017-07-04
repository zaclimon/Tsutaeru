package com.zaclimon.aceiptv.ui.settings;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentCompat;
import android.support.v17.leanback.app.ErrorFragment;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.app.ProgressBarManager;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaclimon.aceiptv.BuildConfig;
import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.util.Constants;

import java.util.Date;
import java.util.List;

/**
 * Created by isaac on 17-07-03.
 */

public class UserInfoGuidedFragment extends GuidedStepFragment implements UserInfoView {

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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ACE_IPTV_PREFERENCES, Context.MODE_PRIVATE);
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
        int id = (int) guidedAction.getId();

         /*
          There seems to have an issue with popBackStackToGuidedStepFragment(), let's just use the
          classical popBackStack().
          */
        if (id == GuidedAction.ACTION_ID_OK) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onConnectionSuccess(String status, Date expirationDate, boolean isTrial, int maxConnections) {
        TextView descriptionView = getGuidanceStylist().getDescriptionView();
        String trial = isTrial ? getString(R.string.yes_text) : getString(R.string.no_text);

        mProgressBarManager.hide();

        getGuidanceStylist().getTitleView().setVisibility(View.VISIBLE);
        descriptionView.setText(getString(R.string.user_info_description, status, expirationDate, trial, Integer.toString(maxConnections)));
        descriptionView.setVisibility(View.VISIBLE);
        getGuidanceStylist().getBreadcrumbView().setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionFailed() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        ErrorFragment errorFragment = new ErrorFragment();
        errorFragment.setImageDrawable(getActivity().getDrawable(R.drawable.lb_ic_sad_cloud));
        errorFragment.setMessage(getString(R.string.user_info_not_accessible));
        errorFragment.setDefaultBackground(true);
        transaction.add(android.R.id.content, errorFragment);
        mProgressBarManager.hide();
        transaction.commit();
    }

    @Override
    public String getUserInfoApiEndpoint() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.ACE_IPTV_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_PREFERENCE, "");
        String password = sharedPreferences.getString(Constants.PASSWORD_PREFERENCE, "");
        return (getString(R.string.ace_user_info_url, username, password));
    }
}
