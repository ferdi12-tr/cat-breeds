package com.example.catbreeds;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheCatApi {

    @GET("?limit=10")
    Call<List<Cat>> getCatsLimit();

    @GET(".")
    Call<List<Cat>> getCats();

    @GET("search")
    Call<List<Cat>> getBySearch(@Query("q") String name);
}
