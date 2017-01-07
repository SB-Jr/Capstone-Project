package com.project.sbjr.showinfodatabase.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Model class to map Movie object to java object;
 * */

public class MovieModel implements Parcelable{

    private String poster_path;
    private String overview;
    private String release_date;
    private int id;
    private String title;
    private String backdrop_path;
    private float vote_average;
    private boolean adult;
    private ArrayList<Genre> genres;

    public MovieModel() {
    }

    public MovieModel(String poster_path, String overview, String release_date, int id, String title, String backdrop_path, boolean adult, ArrayList<Genre> genres) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.id = id;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.adult = adult;
        this.genres = genres;
    }

    protected MovieModel(Parcel in) {
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        id = in.readInt();
        title = in.readString();
        backdrop_path = in.readString();
        vote_average = in.readFloat();
        adult = in.readByte() != 0;
        genres = in.createTypedArrayList(Genre.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeFloat(vote_average);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeTypedList(genres);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean getAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }
}
