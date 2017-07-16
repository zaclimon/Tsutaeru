package com.zaclimon.acetv.data;

/**
 * Offers a basic representation of a audio-visual content.
 *
 * @author zaclimon
 * Creation date: 01/07/17
 */

public class AvContent {

    private String mTitle;
    private String mLogo;
    private String mGroup;
    private String mContentLink;
    private int mId;

    /**
     * Base constructor
     *
     * @param title       the title of the content
     * @param logo        the URL pointing to the related logo of the content
     * @param group       the category in which a content might belong to
     * @param contentLink the URL pointing to the content itself
     */
    public AvContent(String title, String logo, String group, String contentLink) {
        mTitle = title;
        mLogo = logo;
        mGroup = group;
        mContentLink = contentLink;
    }

    /**
     * Constructor for an AvContent having a special identifier.
     *
     * @param title       the title of the content
     * @param logo        the URL pointing to the related logo of the content
     * @param group       the category in which a content might belong to
     * @param contentLink the URL pointing to the content itself
     * @param id          An additional id that can be given to the content
     */
    public AvContent(String title, String logo, String group, String contentLink, int id) {
        mTitle = title;
        mLogo = logo;
        mGroup = group;
        mContentLink = contentLink;
        mId = id;
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
