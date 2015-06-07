package com.ar.tothestars.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ariviere on 23/04/15.
 */
public class Photo implements Parcelable {

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public static final String VIMEO = "player.vimeo.com";
    public static final String YOUTUBE = "www.youtube.com";

    private String name;
    private String url;
    private String explanation;
    private Date date;
    private ArrayList<String> concepts;

    public Photo() {
    }

    private Photo(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        this.explanation = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.concepts = (ArrayList<String>) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.explanation);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeSerializable(this.concepts);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getConcepts() {
        return concepts;
    }

    public void setConcepts(ArrayList<String> concepts) {
        this.concepts = concepts;
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
