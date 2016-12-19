package com.sbjr.project;

import com.sbjr.project.fetch.MoviesFetch;
import com.sbjr.project.fetch.TvShowFetch;
import com.sbjr.project.model.MovieModel;
import com.sbjr.project.response.UpcomingResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Retrofit;

/**
 * Created by sbjr on 19/12/16.
 *
 * Access class to all the movie and tv show apis
 */

public class MovieDatabaseLibrary {

    private static final String BASE_URL="https://api.themoviedb.org/3/";

    private String apiKey;

    private Retrofit retrofit;

    private TvShowFetch tvShowFetch;

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

    public class Movie{

        private MoviesFetch moviesFetch;

        public ArrayList<MovieModel> getUpcomingMovies(){
            if(apiKey==null||!apiKey.isEmpty()){
                return new ArrayList<MovieModel>();
            }
            else {
                if (retrofit == null) {
                    buildClient();
                    moviesFetch = retrofit.create(MoviesFetch.class);
                } else if (moviesFetch==null) {
                    moviesFetch = retrofit.create(MoviesFetch.class);
                }
                UpcomingResponse ur = moviesFetch.getUpcomingMovies(apiKey, 1);
                return ur.getResults();
            }
        }

    }

}
