package com.ar.nasawallpapers.services;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by ariviere on 23/04/15.
 */
public class ServicesGenerator {
    private ServicesGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }
}
