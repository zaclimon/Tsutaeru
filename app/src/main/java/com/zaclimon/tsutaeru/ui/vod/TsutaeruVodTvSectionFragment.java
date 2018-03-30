package com.zaclimon.tsutaeru.ui.vod;

import android.os.Bundle;

import com.zaclimon.tsutaeru.data.RoomContentPersistence;
import com.zaclimon.tsutaeru.ui.components.cardview.PicassoCardViewImageProcessor;
import com.zaclimon.tsutaeru.ui.playback.TsutaeruPlaybackActivity;
import com.zaclimon.xipl.persistence.ContentPersistence;
import com.zaclimon.xipl.ui.components.cardview.CardViewImageProcessor;
import com.zaclimon.xipl.ui.vod.VodPlaybackActivity;
import com.zaclimon.xipl.ui.vod.VodTvSectionFragment;


/**
 * Base class in which VOD-like (Video on demand) fragments can base off in order to have a complete
 * list of content based on their provider's catalog.
 *
 * @author zaclimon
 * Creation date: 05/07/17
 */

public abstract class TsutaeruVodTvSectionFragment extends VodTvSectionFragment {

    private ContentPersistence mContentPersistence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContentPersistence = new RoomContentPersistence(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ContentPersistence getContentPersistence() {
        return (mContentPersistence);
    }

    @Override
    protected CardViewImageProcessor getImageProcessor() {
        return (new PicassoCardViewImageProcessor());
    }

    @Override
    public Class<? extends VodPlaybackActivity> getPlaybackActivity() {
        return (TsutaeruPlaybackActivity.class);
    }
}
