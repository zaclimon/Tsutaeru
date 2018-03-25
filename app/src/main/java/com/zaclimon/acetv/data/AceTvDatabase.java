package com.zaclimon.acetv.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * {@link RoomDatabase} implementation adapted for Ace TV
 *
 * @author zaclimon
 * Creation date: 25/03/18
 */

@Database(version = 1, entities = {RoomAvContent.class})
public abstract class AceTvDatabase extends RoomDatabase {

    /**
     * Name of the database
     */
    private static final String DATABASE_NAME = "ace-tv";

    /**
     * Lock object for synchronization purposes
     */
    private static final Object LOCK = new Object();

    /**
     * Instance of the {@link AceTvDatabase}
     */
    private static volatile AceTvDatabase sInstance;

    /**
     * Gets the {@link AvContentDao} responsible for accessing data regarding contents.
     *
     * @return the DAO responsible for audiovisual content
     */
    abstract public AvContentDao avContentDao();

    /**
     * Gets the instance of the database.
     *
     * @param context the context which will be used to determine the database
     * @return the instance of the database
     */
    public static AceTvDatabase getInstance(Context context) {

        /*
         Singleton pattern code taken from the architecture components codelab here:
         https://codelabs.developers.google.com/codelabs/build-app-with-arch-components/index.html?index=..%2F..%2Findex#7
         */

        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    // Allow queries on the main thread since xipl is executing some of them on it.
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), AceTvDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
                }
            }
        }
        return (sInstance);
    }
}
