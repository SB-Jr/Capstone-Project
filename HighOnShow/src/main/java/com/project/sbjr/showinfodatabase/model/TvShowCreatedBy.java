package com.project.sbjr.showinfodatabase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sbjr on 29/12/16.
 */
public class TvShowCreatedBy implements Parcelable{

    private int id;
    private String name;
    private String profile_path;

    public TvShowCreatedBy() {
    }

    public TvShowCreatedBy(int id, String name, String profile_path) {
        this.id = id;
        this.name = name;
        this.profile_path = profile_path;
    }

    protected TvShowCreatedBy(Parcel in) {
        id = in.readInt();
        name = in.readString();
        profile_path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(profile_path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TvShowCreatedBy> CREATOR = new Creator<TvShowCreatedBy>() {
        @Override
        public TvShowCreatedBy createFromParcel(Parcel in) {
            return new TvShowCreatedBy(in);
        }

        @Override
        public TvShowCreatedBy[] newArray(int size) {
            return new TvShowCreatedBy[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
