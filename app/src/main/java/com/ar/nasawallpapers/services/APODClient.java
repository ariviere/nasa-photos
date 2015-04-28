package com.ar.nasawallpapers.services;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by ariviere on 23/04/15.
 */
public interface APODClient {
    @GET("/nasa/planetary/apod")
    Observable<Response> getPhoto(
            @Query("date") String date,
            @Query("concept_tags") boolean has_concept_tags,
            @Query("api_key") String api_key
    );
}
