package com.zaclimon.acetv.ui.vod;

import android.os.Bundle;

import com.zaclimon.acetv.ui.components.cardview.PicassoCardViewImageProcessor;
import com.zaclimon.acetv.ui.playback.AcePlaybackActivity;
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

public abstract class AceVodTvSectionFragment extends VodTvSectionFragment {

    private ContentPersistence mContentPersistence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContentPersistence = new RealmContentPersistence();
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
        return (AcePlaybackActivity.class);
    }
}
