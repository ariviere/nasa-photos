package com.ar.nasawallpapers.services;

import com.ar.nasawallpapers.models.Photo;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Func1;

/**
 * Created by ariviere on 23/04/15.
 */
public final class APODParser {

    public static final Func1<String, Photo> PARSE_APOD
            = new Func1<String, Photo>() {
        @Override
        public Photo call(String s) {
            if (!s.equals("error")) {
                Photo photo = new Photo();

                try {
                    JSONObject photoJson = new JSONObject(s);
                    photo.setName(photoJson.optString("title"));
                    photo.setUrl(photoJson.optString("url"));
                } catch (JSONException e) {
                    throw new JsonParseException("Error while parsing APOD");
                }

                return photo;
            }

            return null;
        }
    };
}
