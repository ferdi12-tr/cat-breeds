package com.example.catbreeds.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CatDB.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CatDAO getCatDAO();
}
