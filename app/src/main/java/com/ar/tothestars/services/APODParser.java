package com.ar.tothestars.services;

import com.ar.tothestars.models.Photo;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ariviere on 23/04/15.
 */
public final class APODParser {

    public static Photo getPhoto(String s) {
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
}
