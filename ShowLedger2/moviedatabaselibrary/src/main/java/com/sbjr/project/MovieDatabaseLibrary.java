package com.sbjr.project;

import com.sbjr.project.fetch.MoviesFetch;
import com.sbjr.project.fetch.TvShowFetch;
import com.sbjr.project.model.MovieModel;
import com.sbjr.project.response.MovieResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Retrofit;

/**
 * Created by sbjr on 19/12/16.
 *
 * Access class to all the Movie and tv show apis
 */

public class MovieDatabaseLibrary {

    private static final String BASE_URL="https://api.themoviedb.org/3/";

    private String apiKey;
    private Retrofit retrofit;
    
    public Movie Movie;

    public MovieDatabaseLibrary() {
        initApiKey();
        buildClient();
    }

    private void initApiKey(){
        File f = new File("api.key");
        if(f.exists()){
            try {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String s;
                while ((s=br.readLine())!=null){
                    apiKey=s;
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                apiKey=null;
                e.printStackTrace();
            }
        }
        else{
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                apiKey=null;
            }
        }

    }

    private void buildClient(){
        if(apiKey!=null||!apiKey.isEmpty()){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .build();
        }
    }

    public Movie initMovie(){
        Movie = new Movie();
        return Movie;
    }

    private class Movie{

        private MoviesFetch moviesFetch;

        public Movie() {
            if (retrofit == null) {
                buildClient();
                moviesFetch = retrofit.create(MoviesFetch.class);
            } else if (moviesFetch==null) {
                moviesFetch = retrofit.create(MoviesFetch.class);
            }
        }

        /**
         * get the upcoming movie list
         * */
        public ArrayList<MovieModel> getUpcomingMovies(){
            if(apiKey==null||!apiKey.isEmpty()){
                return new ArrayList<MovieModel>();
            }
            else {
                MovieResponse ur = moviesFetch.getUpcomingMovies(apiKey, 1);
                return ur.getResults();
            }
        }

        /**
         * get the movie detail
         * */

        public MovieModel getMovieDetails(int movieId){
            return moviesFetch.getMovieDetailsById(apiKey,movieId);
        }

        /**
         * get the movies matching a search keyword
         * */
        public ArrayList<MovieModel> getMovieBySearch(String query){
            MovieResponse movieResponse = moviesFetch.getMoviesBySearch(apiKey,query);
            return movieResponse.getResults();
        }

    }

    private class TvShow{

        private TvShowFetch tvShowFetch;



    }

}
