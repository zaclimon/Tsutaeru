package com.zaclimon.acetv.ui.search;

import android.os.Bundle;

import com.zaclimon.acetv.data.RealmContentPersistence;
import com.zaclimon.acetv.ui.components.cardview.PicassoCardViewImageProcessor;
import com.zaclimon.xipl.persistence.ContentPersistence;
import com.zaclimon.xipl.ui.components.cardview.CardViewImageProcessor;
import com.zaclimon.xipl.ui.search.ProviderSearchFragment;

/**
 * Concrete implementation of a {@link ProviderSearchFragment} for Ace TV.
 *
 * @author zaclimon
 * Creation date: 16/08/17
 */

public class AceProviderSearchFragment extends ProviderSearchFragment {

    private ContentPersistence mContentPersistence;
    private CardViewImageProcessor mCardViewImageProcessor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContentPersistence = new RealmContentPersistence();
        mCardViewImageProcessor = new PicassoCardViewImageProcessor();
        super.onCreate(savedInstanceState);
    }

    @Override
    public ContentPersistence getContentPersistence() {
        return (mContentPersistence);
    }

    @Override
    public CardViewImageProcessor getCardViewImageProcessor() {
        return (mCardViewImageProcessor);
    }

}
