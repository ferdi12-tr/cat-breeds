package com.example.catbreeds.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CatDAO {

    @Insert
    void insert(CatDB catDB);

    @Query("DELETE FROM CatDB WHERE name = :name")
    void delete(String name);


    @Query("SELECT * FROM CatDB order by name")
    List<CatDB> getAllCatDB();

    @Query("SELECT * FROM CatDB WHERE name = :name")
    CatDB getCatDBByName(String name);
}
