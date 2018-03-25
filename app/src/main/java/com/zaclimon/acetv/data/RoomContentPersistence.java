package com.zaclimon.acetv.data;

import android.content.Context;

import com.zaclimon.xipl.model.AvContent;
import com.zaclimon.xipl.persistence.ContentPersistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of {@link ContentPersistence} applicable for the Room persistence library.
 *
 * @author zaclimon
 * Creation date: 25/03/18
 */

public class RoomContentPersistence implements ContentPersistence {

    /**
     * {@link AvContentDao} used to access the data from {@link AceTvDatabase}
     */
    private AvContentDao mContentDao;

    public RoomContentPersistence(Context context) {
        mContentDao = AceTvDatabase.getInstance(context).avContentDao();
    }

    @Override
    public void insert(AvContent avContent) {
        mContentDao.bulkInsert(new RoomAvContent(avContent));
    }

    @Override
    public void insert(List<AvContent> avContents) {
        RoomAvContent[] tempArray = new RoomAvContent[avContents.size()];
        mContentDao.bulkInsert(convertToRoom(avContents).toArray(tempArray));
    }

    @Override
    public void deleteCategory(String category) {
        mContentDao.deleteCategory(category);
    }

    @Override
    public void deleteAll() {
        mContentDao.deleteAll();
    }

    @Override
    public List<AvContent> getAll() {
        List<RoomAvContent> tempContents = mContentDao.getAll();
        return (convertFromRoom(tempContents));
    }

    @Override
    public List<AvContent> getFromCategory(String category, boolean isAlphabeticallySorted) {
        List<RoomAvContent> tempContents = mContentDao.getFromCategory(category);
        return (convertFromRoom(tempContents));
    }

    @Override
    public List<AvContent> getFromTitle(String title, boolean isAlphabeticallySorted) {
        List<RoomAvContent> tempContents = mContentDao.getFromTitle(title);
        return (convertFromRoom(tempContents));
    }

    @Override
    public long size() {
        return (mContentDao.size());
    }

    @Override
    public long size(String category) {
        return (mContentDao.size(category));
    }

    private List<RoomAvContent> convertToRoom(List<AvContent> contents) {
        List<RoomAvContent> tempList = new ArrayList<>();

        for (AvContent content : contents) {
            tempList.add(new RoomAvContent(content));
        }
        return (tempList);
    }

    private List<AvContent> convertFromRoom(List<RoomAvContent> contents) {

        List<AvContent> tempList = new ArrayList<>();

        for (RoomAvContent content : contents) {
            String title = content.getTitle();
            String logo = content.getLogo();
            String group = content.getGroup();
            String contentLink = content.getContentLink();
            String contentCategory = content.getContentCategory();
            int id = content.getId();
            tempList.add(new AvContent(title, logo, group, contentCategory, contentLink, id));
        }
        return (tempList);
    }

}
