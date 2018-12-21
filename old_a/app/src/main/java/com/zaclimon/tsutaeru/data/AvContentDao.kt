package com.zaclimon.tsutaeru.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * [Dao] used to retrieve [RoomAvContent] for content from a Tsutaeru provider.
 *
 * @author zaclimon
 */
@Dao
interface AvContentDao {

    /**
     * Gets all media content from the database
     *
     * @return all [RoomAvContent] objects from the database.
     */
    @Query("SELECT * FROM contents")
    fun getAll(): List<RoomAvContent>

    /**
     * Inserts contents into the database
     *
     * @param contents which is one or more [RoomAvContent]
     */
    @Insert
    fun bulkInsert(vararg contents: RoomAvContent)

    /**
     * Deletes a category based on [RoomAvContent.contentCategory]
     *
     * @param category the category which will get deleted.
     */
    @Query("DELETE FROM contents WHERE contentCategory = :category")
    fun deleteCategory(category: String)

    /**
     * Deletes all the items that have been persisted in the database.
     */
    @Query("DELETE FROM contents")
    fun deleteAll()

    /**
     * Gives a list of [RoomAvContent] containing a given title.
     *
     * @param title the title of a given content
     * @return the list of contents.
     */
    @Query("SELECT * FROM contents WHERE contentTitle LIKE :title")
    fun getFromTitle(title: String): List<RoomAvContent>

    /**
     * Gives a list of the [RoomAvContent] belonging to a given category.
     *
     * @param category the given category for a content
     * @return the list of contents
     */
    @Query("SELECT * FROM contents WHERE contentCategory = :category ORDER BY contentGroup")
    fun getFromCategory(category: String): List<RoomAvContent>

    /**
     * Gets the total size of the database
     *
     * @return the total number of elements for this medium.
     */
    @Query("SELECT COUNT(*) FROM contents")
    fun size(): Long

    /**
     * Gets the size for a given category in the database
     *
     * @param category the category in which a size is wanted
     * @return the total number of elements for that given category.
     */
    @Query("SELECT COUNT(*) FROM contents WHERE contentCategory = :category")
    fun size(category: String): Long
}