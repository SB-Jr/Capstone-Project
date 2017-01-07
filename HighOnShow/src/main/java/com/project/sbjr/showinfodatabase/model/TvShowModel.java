package com.project.sbjr.showinfodatabase.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sbjr on 27/12/16.
 */

public class TvShowModel implements Parcelable{

    private String backdrop_path;
    private String first_air_date;
    private int id;
    private boolean in_production;
    private String last_air_date;
    private String name;
    private int number_of_episodes;
    private int number_of_seasons;
    private String overview;
    private String poster_path;
    private String status;
    private double vote_average;
    private ArrayList<TvShowCreatedBy> created_by;
    private ArrayList<Genre> genres;
    private ArrayList<TvShowNetwork> networks;
    private ArrayList<TvShowSeason> seasons;


    public TvShowModel() {
    }

    public TvShowModel(String backdrop_path, String first_air_date, int id, boolean in_production, String last_air_date, String name, int number_of_episodes, int number_of_seasons, String overview, String poster_path, String status, double vote_average, ArrayList<TvShowCreatedBy> created_by, ArrayList<Genre> genres, ArrayList<TvShowNetwork> networks, ArrayList<TvShowSeason> seasons) {
        this.backdrop_path = backdrop_path;
        this.first_air_date = first_air_date;
        this.id = id;
        this.in_production = in_production;
        this.last_air_date = last_air_date;
        this.name = name;
        this.number_of_episodes = number_of_episodes;
        this.number_of_seasons = number_of_seasons;
        this.overview = overview;
        this.poster_path = poster_path;
        this.status = status;
        this.vote_average = vote_average;
        this.created_by = created_by;
        this.genres = genres;
        this.networks = networks;
        this.seasons = seasons;
    }

    protected TvShowModel(Parcel in) {
        backdrop_path = in.readString();
        first_air_date = in.readString();
        id = in.readInt();
        in_production = in.readByte() != 0;
        last_air_date = in.readString();
        name = in.readString();
        number_of_episodes = in.readInt();
        number_of_seasons = in.readInt();
        overview = in.readString();
        poster_path = in.readString();
        status = in.readString();
        vote_average = in.readDouble();
        created_by = in.createTypedArrayList(TvShowCreatedBy.CREATOR);
        genres = in.createTypedArrayList(Genre.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backdrop_path);
        dest.writeString(first_air_date);
        dest.writeInt(id);
        dest.writeByte((byte) (in_production ? 1 : 0));
        dest.writeString(last_air_date);
        dest.writeString(name);
        dest.writeInt(number_of_episodes);
        dest.writeInt(number_of_seasons);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeString(status);
        dest.writeDouble(vote_average);
        dest.writeTypedList(created_by);
        dest.writeTypedList(genres);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TvShowModel> CREATOR = new Creator<TvShowModel>() {
        @Override
        public TvShowModel createFromParcel(Parcel in) {
            return new TvShowModel(in);
        }

        @Override
        public TvShowModel[] newArray(int size) {
            return new TvShowModel[size];
        }
    };

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIn_production() {
        return in_production;
    }

    public void setIn_production(boolean in_production) {
        this.in_production = in_production;
    }

    public String getLast_air_date() {
        return last_air_date;
    }

    public void setLast_air_date(String last_air_date) {
        this.last_air_date = last_air_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber_of_episodes() {
        return number_of_episodes;
    }

    public void setNumber_of_episodes(int number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }

    public int getNumber_of_seasons() {
        return number_of_seasons;
    }

    public void setNumber_of_seasons(int number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public ArrayList<TvShowCreatedBy> getCreated_by() {
        return created_by;
    }

    public void setCreated_by(ArrayList<TvShowCreatedBy> created_by) {
        this.created_by = created_by;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public ArrayList<TvShowNetwork> getNetworks() {
        return networks;
    }

    public void setNetworks(ArrayList<TvShowNetwork> networks) {
        this.networks = networks;
    }

    public ArrayList<TvShowSeason> getSeasons() {
        return seasons;
    }

    public void setSeasons(ArrayList<TvShowSeason> seasons) {
        this.seasons = seasons;
    }
}
