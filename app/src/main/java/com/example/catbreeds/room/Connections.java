package com.example.catbreeds.room;

import android.content.Context;

import androidx.room.Room;

public class Connections {
    private static Connections instance;
    private AppDatabase database;

    private Connections (Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, "db_fav_cats")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public static Connections getInstance(Context context) {
        synchronized (Connections.class) { // only one thread can access at one time
            if (instance == null) {
                instance = new Connections(context);
            }
            return instance;
        }
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
