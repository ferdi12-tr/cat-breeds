package com.example.catbreeds.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CatDB")
public class CatDB {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "imgUrl")
    private String imgUrl;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "origin")
    private String origin;

    @ColumnInfo(name = "wikiUrl")
    private String wikiUrl;

    @ColumnInfo(name = "lifeSpan")
    private String lifeSpan;

    @ColumnInfo(name = "dogFriendly")
    private String dogFriendly;

    @ColumnInfo(name = "isFav")
    private boolean isFav;

    @ColumnInfo(name = "imageID")
    private String imageID;

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public String getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(String lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public String getDogFriendly() {
        return dogFriendly;
    }

    public void setDogFriendly(String dogFriendly) {
        this.dogFriendly = dogFriendly;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
