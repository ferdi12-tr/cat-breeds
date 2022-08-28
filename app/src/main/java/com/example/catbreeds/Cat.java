package com.example.catbreeds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

public class Cat {
    @SerializedName("image")
    private ImageClass imageClass;

    private String name;
    private String description;
    private String origin;

    @SerializedName("wikipedia_url")
    private String wikiUrl;

    @SerializedName("life_span")
    private String lifeSpan;

    @SerializedName("dog_friendly")
    private String dogFriendly;

    @SerializedName("reference_image_id")
    private String imageID;

    private boolean isFav;

    public ImageClass getImageClass() {

        return imageClass;
    }

    public String getImageID() {
        return imageID;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOrigin() {
        return origin;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public String getLifeSpan() {
        return lifeSpan;
    }

    public String getDogFriendly() {
        return dogFriendly;
    }
}
