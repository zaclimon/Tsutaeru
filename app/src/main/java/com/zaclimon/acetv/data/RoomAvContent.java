package com.zaclimon.acetv.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.zaclimon.xipl.model.AvContent;

/**
 * Offers a representation of a {@link AvContent} to be usable with the Room persistence library.
 *
 * @author zaclimon
 * Creation date: 25/03/18
 */

@Entity(tableName = "contents")
public class RoomAvContent {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    private String mTitle;
    private String mLogo;
    private String mGroup;
    private String mContentLink;
    private String mContentCategory;

    /**
     * Constructor used for most cases in which the information from a given {@link AvContent}
     * will be copied for Room usage.
     *
     * @param avContent the content that will be copied over.
     */
    @Ignore
    public RoomAvContent(AvContent avContent) {
        mId = avContent.getId();
        mTitle = avContent.getTitle();
        mLogo = avContent.getLogo();
        mGroup = avContent.getGroup();
        mContentLink = avContent.getContentLink();
        mContentCategory = avContent.getContentCategory();
    }

    /**
     * Constructor for an AvContent having a special identifier. Mandatory to use with Room.
     *
     * @param title         the title of the content
     * @param logo          the URL pointing to the related logo of the content
     * @param group         the category in which a content might belong to
     * @param contentCategory the category in which a content might belong to
     * @param contentLink   the URL pointing to the content itself
     * @param id            An additional id that can be given to the content
     */
    public RoomAvContent(String title, String logo, String group, String contentCategory, String contentLink, int id) {
        mId = id;
        mTitle = title;
        mLogo = logo;
        mGroup = group;
        mContentCategory = contentCategory;
        mContentLink = contentLink;
    }

    /**
     * Gets the title
     *
     * @return the title of the content
     */
    public String getTitle() {
        return (mTitle);
    }

    /**
     * Gets the logo
     *
     * @return the logo URL of the content
     */
    public String getLogo() {
        return (mLogo);
    }

    /**
     * Gets the category
     *
     * @return the category of the content
     */
    public String getGroup() {
        return (mGroup);
    }

    /**
     * Gets the content category
     *
     * @return the category in which this content belongs to.
     */
    public String getContentCategory() {
        return (mContentCategory);
    }

    /**
     * Gets the link
     *
     * @return the link to the content
     */
    public String getContentLink() {
        return (mContentLink);
    }

    /**
     * Gets the id
     *
     * @return the id of the content if any
     */
    public int getId() {
        return (mId);
    }

}
