package com.zaclimon.tsutaeru.ui.search;

import android.os.Bundle;

import com.zaclimon.tsutaeru.data.RoomContentPersistence;
import com.zaclimon.tsutaeru.ui.components.cardview.PicassoCardViewImageProcessor;
import com.zaclimon.tsutaeru.ui.playback.TsutaeruPlaybackActivity;
import com.zaclimon.xipl.persistence.ContentPersistence;
import com.zaclimon.xipl.ui.components.cardview.CardViewImageProcessor;
import com.zaclimon.xipl.ui.search.ProviderSearchFragment;
import com.zaclimon.xipl.ui.vod.VodPlaybackActivity;

/**
 * Concrete implementation of a {@link ProviderSearchFragment} for Tsutaeru.
 *
 * @author zaclimon
 * Creation date: 16/08/17
 */

public class TsutaeruProviderSearchFragment extends ProviderSearchFragment {

    private ContentPersistence mContentPersistence;
    private CardViewImageProcessor mCardViewImageProcessor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContentPersistence = new RoomContentPersistence(getActivity());
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

    @Override
    public Class<? extends VodPlaybackActivity> getPlaybackActivity() {
        return (TsutaeruPlaybackActivity.class);
    }

}
