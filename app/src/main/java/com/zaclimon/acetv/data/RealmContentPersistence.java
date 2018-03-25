package com.zaclimon.acetv.data;

import android.util.Log;

import com.zaclimon.xipl.model.AvContent;
import com.zaclimon.xipl.persistence.ContentPersistence;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Concrete implementation of the {@link ContentPersistence} interface based to use with the Realm
 * database.
 *
 * @author zaclimon
 * Creation date: 13/08/17
 */

public class RealmContentPersistence implements ContentPersistence {

    private static final String TITLE_VARIABLE = "mTitle";
    private static final String CONTENT_CATEGORY_VARIABLE = "mContentCategory";
    private static final String GROUP_VARIABLE = "mGroup";

    @Override
    public void insert(final AvContent avContent) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(new RealmAvContent(avContent));
            }
        });
        realm.close();
    }

    @Override
    public void insert(List<AvContent> avContents) {
        Realm realm = Realm.getDefaultInstance();
        final List<RealmAvContent> realmAvContents = convertToRealm(avContents);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(realmAvContents);
            }
        });
        realm.close();
    }

    @Override
    public void deleteCategory(final String category) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmAvContent> results = realm.where(RealmAvContent.class).equalTo(CONTENT_CATEGORY_VARIABLE, category).findAll();
                results.deleteAllFromRealm();
            }
        });
        realm.close();
    }

    @Override
    public void deleteAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(RealmAvContent.class).findAll().deleteAllFromRealm();
            }
        });
        realm.close();
    }

    @Override
    public List<AvContent> getAll() {
        Realm realm = Realm.getDefaultInstance();
        List<AvContent> tempList = convertFromRealm(realm.where(RealmAvContent.class).findAll());
        realm.close();
        return (tempList);
    }

    @Override
    public List<AvContent> getFromCategory(String category, boolean isAlphabeticallySorted) {
        Realm realm = Realm.getDefaultInstance();
        List<AvContent> tempList;

        if (isAlphabeticallySorted) {
            tempList = convertFromRealm(realm.where(RealmAvContent.class).equalTo(CONTENT_CATEGORY_VARIABLE, category).findAllSorted(GROUP_VARIABLE));
        } else {
            tempList = convertFromRealm(realm.where(RealmAvContent.class).equalTo(CONTENT_CATEGORY_VARIABLE, category).findAll());
        }

        realm.close();
        return (tempList);

    }

    @Override
    public List<AvContent> getFromTitle(String title, boolean isAlphabeticallySorted) {
        Realm realm = Realm.getDefaultInstance();
        List<AvContent> tempList;

        if (isAlphabeticallySorted) {
            tempList = convertFromRealm(realm.where(RealmAvContent.class).contains(TITLE_VARIABLE, title, Case.INSENSITIVE).findAllSorted(TITLE_VARIABLE));
        } else {
            tempList = convertFromRealm(realm.where(RealmAvContent.class).contains(TITLE_VARIABLE, title, Case.INSENSITIVE).findAll());
        }
        Log.d(getClass().getSimpleName(), "templist size: " + tempList.size());
        realm.close();

        return (tempList);
    }

    @Override
    public long size() {
        Realm realm = Realm.getDefaultInstance();
        long tempSize = realm.where(RealmAvContent.class).count();
        realm.close();
        return (tempSize);
    }

    @Override
    public long size(String category) {
        Realm realm = Realm.getDefaultInstance();
        long tempSize = realm.where(RealmAvContent.class).equalTo(CONTENT_CATEGORY_VARIABLE, category).count();
        realm.close();
        return (tempSize);
    }

    private List<RealmAvContent> convertToRealm(List<AvContent> contents) {

        List<RealmAvContent> tempList = new ArrayList<>();

        for (AvContent content : contents) {
            tempList.add(new RealmAvContent(content));
        }
        return (tempList);
    }

    private List<AvContent> convertFromRealm(List<RealmAvContent> contents) {

        List<AvContent> tempList = new ArrayList<>();

        for (RealmAvContent content : contents) {
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
