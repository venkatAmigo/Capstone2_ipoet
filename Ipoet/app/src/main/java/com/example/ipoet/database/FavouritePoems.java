package com.example.ipoet.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorite_poems")
public class FavouritePoems {

    @NonNull
    @PrimaryKey
    private
    String title;

    private String author;
    private String poem;
    public String getAuthor() {
        return author;
    }

    public String getPoem() {
        return poem;
    }

    public String getTitle() {
        return title;
    }


    public FavouritePoems( String title, String author, String poem) {
        this.title = title;
        this.author = author;
        this.poem = poem;


    }
}
