package com.zaclimon.tsutaeru.ui.components.cardview

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.zaclimon.xipl.ui.components.cardview.CardViewImageProcessor

/**
 * Concrete implementation of [CardViewImageProcessor] that uses Picasso as
 * an underlying backend library.
 *
 * @author zaclimon
 */
class PicassoCardViewImageProcessor : CardViewImageProcessor {

    override fun loadImage(url: String?, cardViewWidth: Int, cardViewHeight: Int, imageView: ImageView?) {
        Picasso.with(imageView?.context).load(url).resize(cardViewWidth, cardViewHeight).into(imageView)
    }

}