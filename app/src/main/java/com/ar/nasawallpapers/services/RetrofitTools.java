package com.ar.nasawallpapers.services;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.functions.Func1;

/**
 * Created by ariviere on 28/04/15.
 */
public class RetrofitTools {

    public static final String TAG = "RetrofitTools";
    /**
     * Allow an {@link rx.Observable<retrofit.client.Response>} to save the Response body for offline usage.
     */
    public static Func1<Response, String> transformToString = new Func1<Response, String>() {
        @Override
        public String call(Response response) {

            String jsonBody;

            //Try to get response body
            BufferedReader reader;
            StringBuilder sb = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(response.getBody().in(),
                        Charset.forName("UTF-8")));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException : " + e.getMessage());
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException : " + e.getMessage());
            }


            jsonBody = sb.toString();
            return jsonBody;
        }
    };

    public static Func1<Throwable, String> errorHandling = new Func1<Throwable, String>() {
        @Override
        public String call(Throwable throwable) {
            return "error";
        }
    };
}
