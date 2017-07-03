package com.zaclimon.aceiptv.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.data.AvContent;
import com.zaclimon.aceiptv.settings.SettingsObjectAdapter;

/**
 * Custom {@link Presenter} class that is used to show {@link ImageCardView}
 * in a given list. (Mostly Leanback related UI widgets)
 *
 * @author zaclimon
 * Creation date: 21/06/17
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
        final ImageCardView imageCardView = (ImageCardView) viewHolder.view;
        Context context = viewHolder.view.getContext();

        if (item instanceof Bundle) {
            // We're dealing with a Settings menu value
            Bundle settingsBundle = (Bundle) item;
            String name = context.getString(settingsBundle.getInt(SettingsObjectAdapter.BUNDLE_SETTINGS_NAME_ID));
            Drawable drawable = context.getDrawable(settingsBundle.getInt(SettingsObjectAdapter.BUNDLE_SETTINGS_DRAWABLE_ID));
            imageCardView.setTitleText(name);
            imageCardView.setMainImage(drawable);
        } else if (item instanceof AvContent) {
            // We're dealing with an AvContent item (TvCatchup/VOD)
            AvContent avContent = (AvContent) item;
            Target target = getImageViewTarget(imageCardView);
            imageCardView.setTitleText(avContent.getTitle());
            Picasso.with(imageCardView.getContext()).load(avContent.getLogo()).into(target);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    private Target getImageViewTarget(final ImageCardView imageCardView) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable drawable = new BitmapDrawable(imageCardView.getResources(), bitmap);
                imageCardView.setMainImage(drawable);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                imageCardView.setMainImage(errorDrawable, true);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                imageCardView.setMainImage(placeHolderDrawable, true);
            }
        };
        return (target);
    }

}