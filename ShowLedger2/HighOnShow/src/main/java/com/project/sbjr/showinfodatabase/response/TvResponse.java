package com.project.sbjr.showinfodatabase.response;

import com.project.sbjr.showinfodatabase.model.TvShowModel;

import java.util.ArrayList;

/**
 * Created by sbjr on 29/12/16.
 */

public class TvResponse {

    private int page;
    private int total_results;
    private int total_pages;
    private ArrayList<TvShowModel> results;

    public TvResponse(int page, int total_results, int total_pages, ArrayList<TvShowModel> results) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.results = results;
    }

    public TvResponse() {
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<TvShowModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<TvShowModel> results) {
        this.results = results;
    }
}
