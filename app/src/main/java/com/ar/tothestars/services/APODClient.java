package com.ar.tothestars.services;

import com.ar.tothestars.models.APODPhoto;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ariviere on 23/04/15.
 */
public interface APODClient {
    @GET("/planetary/apod")
    void getPhoto(
            @Query("date") String date,
            @Query("concept_tags") boolean has_concept_tags,
            @Query("api_key") String api_key,
            Callback<APODPhoto> callback
    );
}
