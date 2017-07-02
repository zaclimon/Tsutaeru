package com.zaclimon.aceiptv.data;

/**
 * Created by isaac on 17-07-01.
 */

public class AvContent {

    private String mTitle;
    private String mLogo;
    private String mGroup;
    private String mContentLink;

    public AvContent(String title, String logo, String group, String contentLink) {
        mTitle = title;
        mLogo = logo;
        mGroup = group;
        mContentLink = contentLink;
    }

    public String getTitle() {
        return (mTitle);
    }

    public String getLogo() {
        return (mLogo);
    }

    public String getGroup() {
        return (mGroup);
    }

    public String getContentLink() {
        return (mContentLink);
    }
}
