package com.ar.tothestars.services;

import retrofit.RestAdapter;

/**
 * Created by ariviere on 07/06/15.
 */
public class APODManager {

    public static final String APOD_URL = "https://api.data.gov";

    public static APODClient getClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(APOD_URL)
                .build();

        return restAdapter.create(APODClient.class);
    }
}
