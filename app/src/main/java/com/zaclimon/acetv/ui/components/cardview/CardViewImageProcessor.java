package com.zaclimon.acetv.ui.components.cardview;

import android.widget.ImageView;

/**
 * Interface that loads the image to an ImageCardView.
 *
 * @author zaclimon
 * Creation date: 07/07/17
 */

public interface CardViewImageProcessor {

    /**
     * Loads an image from a location to a given ImageView
     *
     * @param url            the link to the image.
     * @param cardViewWidth  the width of the image region of CardView. The unit is in pixels.
     * @param cardViewHeight the height of the image region of the CardView. The unit is in pixels.
     * @param imageView      the ImageView in which the image will be loaded.
     */
    void loadImage(String url, int cardViewWidth, int cardViewHeight, ImageView imageView);
}
