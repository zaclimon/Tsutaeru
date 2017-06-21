package com.zaclimon.aceiptv.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;

import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.settings.SettingsObjectAdapter;

/**
 * Created by isaac on 17-06-21.
 */

public class CardViewPresenter extends Presenter {

    private static final int CARDVIEW_WIDTH = 200;
    private static final int CARDVIEW_HEIGHT = 200;

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageCardView imageCardView = new ImageCardView(parent.getContext());
        imageCardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ALWAYS);
        imageCardView.setFocusable(true);
        imageCardView.setFocusableInTouchMode(true);
        imageCardView.setMainImageDimensions(CARDVIEW_WIDTH, CARDVIEW_HEIGHT);
        return (new ViewHolder(imageCardView));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ImageCardView imageCardView = (ImageCardView) viewHolder.view;
        Context context = viewHolder.view.getContext();

        if (item instanceof Bundle) {
            // We're dealing with a Settings menu value
            Bundle settingsBundle = (Bundle) item;
            String name = context.getString(settingsBundle.getInt(SettingsObjectAdapter.BUNDLE_SETTINGS_NAME_ID));
            String description = context.getString(settingsBundle.getInt(SettingsObjectAdapter.BUNDLE_SETTINGS_DESCRIPTION_ID));
            Drawable drawable = context.getDrawable(settingsBundle.getInt(SettingsObjectAdapter.BUNDLE_SETTINGS_DRAWABLE_ID));
            imageCardView.setTitleText(name);
            imageCardView.setContentText(description);
            imageCardView.setMainImage(drawable);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}