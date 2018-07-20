package com.inc.mountzoft.popularmoviesstage_2;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {RoomMovieEntity.class}, version = 1)
public abstract class RoomMovieDatabase extends RoomDatabase {

    private static RoomMovieDatabase INSTANCE;

        public abstract RoomMovieDao roomMovieDao();

    private static final Object LOCK = new Object();
    public static RoomMovieDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), RoomMovieDatabase.class, "user-database")
                                // allow queries on the main thread.
                                .allowMainThreadQueries()
                                .build();
            }
        }
        return INSTANCE;
    }
}
