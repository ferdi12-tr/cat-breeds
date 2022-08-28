package com.example.catbreeds;

import com.google.gson.annotations.SerializedName;

public class ImageClass {

    @SerializedName("url")
    public String url;

    public ImageClass(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
