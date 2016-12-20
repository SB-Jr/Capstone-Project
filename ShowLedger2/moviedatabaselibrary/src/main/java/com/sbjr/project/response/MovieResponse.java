package com.sbjr.project.response;

import com.sbjr.project.model.MovieModel;

import java.util.ArrayList;

/**
 * Created by sbjr on 19/12/16.
 *
 * Response map class to map the response of
 * https://api.themoviedb.org/3/movie/upcoming?api_key="KEY"&language=en-US&page="PAGE_NO"
 */

public class MovieResponse {

    private int page;
    private ArrayList<MovieModel> results;
    private int total_pages;

    public MovieResponse() {
    }

    public MovieResponse(int page, ArrayList<MovieModel> results, int total_pages) {
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<MovieModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieModel> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
