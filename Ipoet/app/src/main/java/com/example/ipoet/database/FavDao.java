package com.example.ipoet.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface FavDao {

    @Insert(onConflict = REPLACE)
    void insertFav(FavouritePoems favouritePoems);

    @Delete
    void deleteData(FavouritePoems favouritePoems);

    @Query("Select * From favorite_poems")
    LiveData<List<FavouritePoems>> getAllFavData();

@Query("SELECT *FROM favorite_poems WHERE title = :title LIMIT 1")
        FavouritePoems isItInDatabase(String title);
}
