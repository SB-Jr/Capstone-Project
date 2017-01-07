package com.project.sbjr.showinfodatabase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sbjr on 1/1/17.
 */

public class TvShowEpisode implements Parcelable{

    private String air_date;
    private int episode_number;
    private String name;
    private String overview;
    private int id;
    private int season_number;
    private String still_path;
    private double vote_average;

    public TvShowEpisode() {
    }

    public TvShowEpisode(String air_date, int episode_number, String name, String overview, int id, int season_number, String still_path, double vote_average) {
        this.air_date = air_date;
        this.episode_number = episode_number;
        this.name = name;
        this.overview = overview;
        this.id = id;
        this.season_number = season_number;
        this.still_path = still_path;
        this.vote_average = vote_average;
    }

    protected TvShowEpisode(Parcel in) {
        air_date = in.readString();
        episode_number = in.readInt();
        name = in.readString();
        overview = in.readString();
        id = in.readInt();
        season_number = in.readInt();
        still_path = in.readString();
        vote_average = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(air_date);
        dest.writeInt(episode_number);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeInt(id);
        dest.writeInt(season_number);
        dest.writeString(still_path);
        dest.writeDouble(vote_average);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TvShowEpisode> CREATOR = new Creator<TvShowEpisode>() {
        @Override
        public TvShowEpisode createFromParcel(Parcel in) {
            return new TvShowEpisode(in);
        }

        @Override
        public TvShowEpisode[] newArray(int size) {
            return new TvShowEpisode[size];
        }
    };

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public int getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(int episode_number) {
        this.episode_number = episode_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    public String getStill_path() {
        return still_path;
    }

    public void setStill_path(String still_path) {
        this.still_path = still_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }
}
