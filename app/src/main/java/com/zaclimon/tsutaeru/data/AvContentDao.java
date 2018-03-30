package com.zaclimon.tsutaeru.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * {@link Dao} used to retrieve {@link RoomAvContent} for a Tsutaeru provider contents.
 *
 * @author zaclimon
 * Creation date: 25/03/18
 */

@Dao
public interface AvContentDao {

    /**
     * Gets all media content from the database
     *
     * @return all {@link RoomAvContent} objects from the database.
     */
    @Query("SELECT * FROM contents")
    List<RoomAvContent> getAll();

    /**
     * Inserts contents into the database
     *
     * @param contents which is one or more {@link RoomAvContent}
     */
    @Insert
    void bulkInsert(RoomAvContent... contents);

    /**
     * Deletes a category based on {@link RoomAvContent#mContentCategory}
     *
     * @param category the category which will get deleted.
     */
    @Query("DELETE FROM contents WHERE mContentCategory = :category")
    void deleteCategory(String category);

    /**
     * Deletes all the items that have been persisted in the database.
     */
    @Query("DELETE FROM contents")
    void deleteAll();

    /**
     * Gives a list of {@link RoomAvContent} containing a given title.
     *
     * @param title the title of a given content
     * @return the list of contents.
     */
    @Query("SELECT * FROM contents WHERE mTitle LIKE :title")
    List<RoomAvContent> getFromTitle(String title);

    /**
     * Gives a list of the {@link RoomAvContent} belonging to a given category.
     *
     * @param category the given category for a content
     * @return the list of contents
     */
    @Query("SELECT * FROM contents WHERE mContentCategory = :category ORDER BY mGroup")
    List<RoomAvContent> getFromCategory(String category);

    /**
     * Gets the total size of the database
     *
     * @return the total number of elements for this medium.
     */
    @Query("SELECT COUNT(*) FROM contents")
    long size();

    /**
     * Gets the size for a given category in the database
     *
     * @param category the category in which a size is wanted
     * @return the total number of elements for that given category.
     */
    @Query("SELECT COUNT(*) FROM contents WHERE mContentCategory = :category")
    long size(String category);
}
