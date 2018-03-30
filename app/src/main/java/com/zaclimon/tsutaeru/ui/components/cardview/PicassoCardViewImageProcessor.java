package com.zaclimon.tsutaeru.ui.components.cardview;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zaclimon.xipl.ui.components.cardview.CardViewImageProcessor;

/**
 * Concrete implementation of {@link CardViewImageProcessor} that uses Picasso as
 * an underlying backend library.
 *
 * @author zaclimon
 * Creation date: 07/07/17
 */

public class PicassoCardViewImageProcessor implements CardViewImageProcessor {

    @Override
    public void loadImage(String url, int cardViewWidth, int cardViewHeight, ImageView imageView) {
        Picasso.with(imageView.getContext()).load(url).resize(cardViewWidth, cardViewHeight).into(imageView);
    }

}
