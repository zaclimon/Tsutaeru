package com.zaclimon.aceiptv.ui.presenter.cardview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.zaclimon.aceiptv.data.AvContent;
import com.zaclimon.aceiptv.ui.settings.SettingsObjectAdapter;
import com.zaclimon.aceiptv.util.ActivityUtil;

/**
 * Custom {@link Presenter} class that is used to show {@link ImageCardView}
 * in a given list. (Mostly Leanback related UI widgets)
 *
 * @author zaclimon
 * Creation date: 21/06/17
 */

public class CardViewPresenter extends Presenter {

    /*
      Let's use Google's square cards example CardView for the dimensions found here:

      https://github.com/googlesamples/leanback-showcase/blob/master/app/src/main/res/values/dims.xml
     */
    private static final int CARDVIEW_WIDTH_DP = 128;
    private static final int CARDVIEW_HEIGHT_DP = 128;

    private CardViewImageProcessor mCardViewImageProcessor;

    /**
     * Default constructor
     */
    public CardViewPresenter() {
        mCardViewImageProcessor = null;
    }

    /**
     * Additional constructor if processing an image from an external resource is required
     * @param cardViewImageProcessor the processor which will be used to retrieve an image.
     */
    public CardViewPresenter(CardViewImageProcessor cardViewImageProcessor) {
        mCardViewImageProcessor = cardViewImageProcessor;
    }

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageCardView imageCardView = new ImageCardView(parent.getContext());

        int widthPixels = ActivityUtil.dpToPixel(CARDVIEW_WIDTH_DP, imageCardView.getContext());
        int heightPixels = ActivityUtil.dpToPixel(CARDVIEW_HEIGHT_DP, imageCardView.getContext());

        imageCardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ALWAYS);
        imageCardView.setFocusable(true);
        imageCardView.setFocusableInTouchMode(true);
        imageCardView.setMainImageDimensions(widthPixels, heightPixels);
        return (new ViewHolder(imageCardView));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        final ImageCardView imageCardView = (ImageCardView) viewHolder.view;
        Context context = viewHolder.view.getContext();

        int widthPixels = ActivityUtil.dpToPixel(CARDVIEW_WIDTH_DP, context);
        int heightPixels = ActivityUtil.dpToPixel(CARDVIEW_HEIGHT_DP, context);

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
            imageCardView.setTitleText(avContent.getTitle());
            if (!TextUtils.isEmpty(avContent.getLogo()) && mCardViewImageProcessor != null) {
                mCardViewImageProcessor.loadImage(avContent.getLogo(), widthPixels, heightPixels, imageCardView.getMainImageView());
            }
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}