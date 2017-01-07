package com.project.sbjr.showinfodatabase.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sbjr on 29/12/16.
 */
public class TvShowSeason implements Parcelable{

    private String air_date;
    private int episode_count;
    private int id;
    private String poster_path;
    private int season_number;
    private ArrayList<TvShowEpisode> episodes;

    public TvShowSeason() {
    }

    public TvShowSeason(String air_date, int episode_count, int id, String poster_path, int season_number, ArrayList<TvShowEpisode> episodes) {
        this.air_date = air_date;
        this.episode_count = episode_count;
        this.id = id;
        this.poster_path = poster_path;
        this.season_number = season_number;
        this.episodes = episodes;
    }


    protected TvShowSeason(Parcel in) {
        air_date = in.readString();
        episode_count = in.readInt();
        id = in.readInt();
        poster_path = in.readString();
        season_number = in.readInt();
        episodes = in.createTypedArrayList(TvShowEpisode.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(air_date);
        dest.writeInt(episode_count);
        dest.writeInt(id);
        dest.writeString(poster_path);
        dest.writeInt(season_number);
        dest.writeTypedList(episodes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TvShowSeason> CREATOR = new Creator<TvShowSeason>() {
        @Override
        public TvShowSeason createFromParcel(Parcel in) {
            return new TvShowSeason(in);
        }

        @Override
        public TvShowSeason[] newArray(int size) {
            return new TvShowSeason[size];
        }
    };

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public int getEpisode_count() {
        return episode_count;
    }

    public void setEpisode_count(int episode_count) {
        this.episode_count = episode_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    public ArrayList<TvShowEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<TvShowEpisode> episodes) {
        this.episodes = episodes;
    }
}
