package com.zaclimon.tsutaeru.ui.vod

import android.os.Bundle
import com.zaclimon.tsutaeru.data.RoomContentPersistence
import com.zaclimon.tsutaeru.ui.components.cardview.PicassoCardViewImageProcessor
import com.zaclimon.tsutaeru.ui.playback.TsutaeruPlaybackActivity
import com.zaclimon.xipl.persistence.ContentPersistence
import com.zaclimon.xipl.ui.components.cardview.CardViewImageProcessor
import com.zaclimon.xipl.ui.vod.VodPlaybackActivity
import com.zaclimon.xipl.ui.vod.VodTvSectionFragment

/**
 * Base class in which VOD-like (Video on demand) fragments can base off in order to have a complete
 * list of content based on their provider's catalog.
 *
 * @author zaclimon
 */
abstract class TsutaeruVodTvSectionFragment : VodTvSectionFragment() {

    private lateinit var fragmentContentPersistence: ContentPersistence

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentContentPersistence = RoomContentPersistence(context)
        super.onCreate(savedInstanceState)
    }

    override fun getContentPersistence(): ContentPersistence {
        return fragmentContentPersistence
    }

    override fun getImageProcessor(): CardViewImageProcessor {
        return PicassoCardViewImageProcessor()
    }

    override fun getPlaybackActivity(): Class<out VodPlaybackActivity> {
        return TsutaeruPlaybackActivity::class.java
    }


}