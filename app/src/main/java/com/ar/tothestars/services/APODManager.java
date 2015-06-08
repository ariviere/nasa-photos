package com.ar.tothestars.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import retrofit.RestAdapter;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by ariviere on 07/06/15.
 */
public class APODManager {

    public static final String APOD_URL = "https://api.data.gov";

    public static APODClient getClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(APOD_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return restAdapter.create(APODClient.class);
    }
}
