package com.zaclimon.tsutaeru.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.zaclimon.xipl.model.AvContent

/**
 * Offers a representation of a [AvContent] to be usable with the Room persistence library.
 *
 * @author zaclimon
 */
@Entity(tableName = "contents")
data class RoomAvContent constructor(
        @PrimaryKey(autoGenerate = true)
        val contentId: Int,
        val contentTitle: String,
        val contentLogo: String,
        val contentGroup: String,
        val contentLink: String,
        val contentCategory: String
) {
    /**
     * Constructor used for most cases in which the information from a given [AvContent]
     * will be copied for Room usage.
     *
     * @param content the content that will be copied over.
     */
    @Ignore
    constructor(content: AvContent) : this(content.id, content.title, content.logo, content.group, content.contentLink, content.contentCategory)

}