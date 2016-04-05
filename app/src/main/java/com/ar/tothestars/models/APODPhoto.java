package com.ar.tothestars.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ariviere on 23/04/15.
 */
public class APODPhoto implements Parcelable {

    public static final Parcelable.Creator<APODPhoto> CREATOR = new Parcelable.Creator<APODPhoto>() {
        public APODPhoto createFromParcel(Parcel source) {
            return new APODPhoto(source);
        }

        public APODPhoto[] newArray(int size) {
            return new APODPhoto[size];
        }
    };

    public static final String VIMEO = "player.vimeo.com";
    public static final String YOUTUBE = "www.youtube.com";

    private String title;
    private String url;
    private String hdurl;
    private String explanation;
    private String date;

    public APODPhoto() {
    }

    private APODPhoto(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.hdurl = in.readString();
        this.explanation = in.readString();
        this.date = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.hdurl);
        dest.writeString(this.explanation);
        dest.writeString(this.date);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHdurl() {
        return hdurl;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    public boolean isValid() {
        if (getUrl().contains(VIMEO)) {
            return false;
        } else if (getUrl().contains(YOUTUBE)) {
            return false;
        }

        return true;
    }

}
