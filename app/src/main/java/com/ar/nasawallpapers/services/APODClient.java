package com.ar.nasawallpapers.services;

import com.ar.nasawallpapers.models.Photo;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ariviere on 23/04/15.
 */
public interface APODClient {
    @GET("/nasa/planetary/apod")
    Photo photo(
            @Query("date") String date,
            @Query("concept_tags") boolean has_concept_tags,
            @Query("api_key") String api_key
    );
}
