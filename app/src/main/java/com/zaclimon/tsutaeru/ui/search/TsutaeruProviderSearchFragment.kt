package com.zaclimon.tsutaeru.ui.search

import android.os.Bundle
import com.zaclimon.tsutaeru.data.RoomContentPersistence
import com.zaclimon.tsutaeru.ui.components.cardview.PicassoCardViewImageProcessor
import com.zaclimon.tsutaeru.ui.playback.TsutaeruPlaybackActivity
import com.zaclimon.xipl.persistence.ContentPersistence
import com.zaclimon.xipl.ui.components.cardview.CardViewImageProcessor
import com.zaclimon.xipl.ui.search.ProviderSearchFragment
import com.zaclimon.xipl.ui.vod.VodPlaybackActivity

/**
 * Concrete implementation of a [ProviderSearchFragment] for Tsutaeru.
 *
 * @author zaclimon
 * Creation date: 16/08/17
 */
class TsutaeruProviderSearchFragment : ProviderSearchFragment() {

    private lateinit var searchContentPersistence: ContentPersistence
    private lateinit var searchCardViewImageProcessor: CardViewImageProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        searchContentPersistence = RoomContentPersistence(context)
        searchCardViewImageProcessor = PicassoCardViewImageProcessor()
        super.onCreate(savedInstanceState)
    }

    override fun getCardViewImageProcessor(): CardViewImageProcessor {
        return searchCardViewImageProcessor
    }

    override fun getContentPersistence(): ContentPersistence {
        return searchContentPersistence
    }

    override fun getPlaybackActivity(): Class<out VodPlaybackActivity> {
        return TsutaeruPlaybackActivity::class.java
    }
}