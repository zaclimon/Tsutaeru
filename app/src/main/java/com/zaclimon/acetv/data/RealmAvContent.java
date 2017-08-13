package com.zaclimon.acetv.data;

import com.zaclimon.xipl.model.AvContent;

import io.realm.RealmObject;

/**
 * Offers representation of a {@link AvContent} content usable for Realm.
 *
 * @author zaclimon
 * Creation date: 01/07/17
 */

public class RealmAvContent extends RealmObject {

    private String mTitle;
    private String mLogo;
    private String mGroup;
    private String mContentLink;
    private String mContentCategory;
    private int mId;

    /**
     * Default constructor. Mandatory for using with Realm.
     */
    public RealmAvContent() {
    }

    /**
     * Constructor used for most cases in which the information from a given {@link AvContent}
     * will be copied for Realm usage.
     *
     * @param avContent the content that will be copied over.
     */
    public RealmAvContent(AvContent avContent) {
        mTitle = avContent.getTitle();
        mLogo = avContent.getLogo();
        mGroup = avContent.getGroup();
        mContentLink = avContent.getContentLink();
        mContentCategory = avContent.getContentCategory();
        mId = avContent.getId();
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
