package com.zaclimon.tsutaeru.data

import android.content.Context
import com.zaclimon.xipl.model.AvContent
import com.zaclimon.xipl.persistence.ContentPersistence

/**
 * Concrete implementation of [ContentPersistence] applicable for the Room persistence library.
 *
 * @property contentDao the DAO that will be used for interaction between the application and the Room database
 *
 * @author zaclimon
 */
class RoomContentPersistence(context: Context) : ContentPersistence {

    private val contentDao: AvContentDao = TsutaeruDatabase.getInstance(context).avContentDao()

    override fun insert(avContent: AvContent) {
        contentDao.bulkInsert(RoomAvContent(avContent))
    }

    override fun insert(avContents: MutableList<AvContent>) {
        contentDao.bulkInsert(*convertToRoom(avContents).toTypedArray())
    }

    override fun deleteCategory(category: String) {
        contentDao.deleteCategory(category)
    }

    override fun deleteAll() {
        contentDao.deleteAll()
    }

    override fun getAll(): MutableList<AvContent> {
        return convertFromRoom(contentDao.getAll()).toMutableList()
    }

    override fun getFromTitle(title: String, isAlphabeticallySorted: Boolean): MutableList<AvContent> {
        return convertFromRoom(contentDao.getFromTitle(title)).toMutableList()
    }

    override fun getFromCategory(category: String, isAlphabeticallySorted: Boolean): MutableList<AvContent> {
        return convertFromRoom(contentDao.getFromCategory(category)).toMutableList()
    }

    override fun size(): Long {
        return contentDao.size()
    }

    override fun size(category: String): Long {
        return contentDao.size(category)
    }

    /**
     * Converts a given list of [AvContent]s to be usable for [RoomAvContent]
     *
     * @param contents the list containing [AvContent]s
     * @return the list of converted [RoomAvContent]s
     */
    private fun convertToRoom(contents: List<AvContent>): List<RoomAvContent> {
        val tempList = mutableListOf<RoomAvContent>()

        for (content in contents) {
            tempList.add(RoomAvContent(content))
        }
        return tempList
    }

    /**
     * Converts a given list of [RoomAvContent]s to be usable for [AvContent]
     *
     * @param contents the list containing [RoomAvContent]
     * @return the list of converted [AvContent]s
     */
    private fun convertFromRoom(contents: List<RoomAvContent>): List<AvContent> {
        val tempList = mutableListOf<AvContent>()

        for (content in contents) {
            val id = content.contentId
            val title = content.contentTitle
            val logo = content.contentLogo
            val group = content.contentGroup
            val link = content.contentLink
            val category = content.contentCategory
            tempList.add(AvContent(title, logo, group, link, category, id))
        }
        return tempList
    }
}