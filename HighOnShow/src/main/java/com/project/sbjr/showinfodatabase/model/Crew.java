package com.project.sbjr.showinfodatabase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sbjr on 28/12/16.
 */

public class Crew implements Parcelable{

    private int id;
    private String name;
    private String job;


    public Crew() {
    }

    public Crew(int id, String name, String job) {
        this.id = id;
        this.name = name;
        this.job = job;
    }

    protected Crew(Parcel in) {
        id = in.readInt();
        name = in.readString();
        job = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(job);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Crew> CREATOR = new Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel in) {
            return new Crew(in);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
