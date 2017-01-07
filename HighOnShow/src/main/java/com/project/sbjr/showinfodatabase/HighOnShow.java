package com.project.sbjr.showinfodatabase;


import android.view.View;

import com.project.sbjr.showinfodatabase.fetch.MoviesFetch;
import com.project.sbjr.showinfodatabase.fetch.TvShowFetch;
import com.project.sbjr.showinfodatabase.handler.ShowHandler;
import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showinfodatabase.model.TvShowSeason;
import com.project.sbjr.showinfodatabase.response.CreditResponse;
import com.project.sbjr.showinfodatabase.response.MovieResponse;
import com.project.sbjr.showinfodatabase.response.TvResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sbjr on 19/12/16.
 *
 * Access class to all the Movie and tv show apis
 */

public class HighOnShow {

    private static final String BASE_URL="https://api.themoviedb.org/3/";

    private String apiKey;
    private Retrofit retrofit;
    
    public Movie Movie;
    public TvShow TvShow;

    public HighOnShow(String key) {
        apiKey = key;
        buildClient();
    }

    private void buildClient(){
        if(apiKey!=null||!apiKey.isEmpty()){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }


    public Movie initMovie(){
        Movie = new Movie(this);
        return Movie;
    }

    public TvShow initTvShow(){
        TvShow = new TvShow(this);
        return TvShow;
    }

    public class Movie{

        private HighOnShow highOnShow;

        private MoviesFetch moviesFetch;

        public Movie(HighOnShow highOnShow) {

            this.highOnShow = highOnShow;

            if (retrofit == null) {
                highOnShow.buildClient();
                moviesFetch = retrofit.create(MoviesFetch.class);
            } else if (moviesFetch==null) {
                moviesFetch = retrofit.create(MoviesFetch.class);
            }
        }

        /**
         * get the upcoming movie list
         * */
        public void getUpcomingMovies(final View onCompleteShow, final View onDataFetchShow, final View onFailureShow, ShowHandler<MovieResponse> handler){

            if(onCompleteShow!=null) {
                onCompleteShow.setVisibility(View.GONE);
            }
            if(onFailureShow!=null) {
                onFailureShow.setVisibility(View.GONE);
            }
            if(onDataFetchShow!=null) {
                onDataFetchShow.setVisibility(View.VISIBLE);
            }

            if(highOnShow.apiKey==null||highOnShow.apiKey.length()==0){
                if(onFailureShow!=null) {
                    onFailureShow.setVisibility(View.VISIBLE);
                }
                if(onDataFetchShow!=null) {
                    onDataFetchShow.setVisibility(View.GONE);
                }
                return;
            }
            handler.initViews(onCompleteShow,onDataFetchShow,onFailureShow);
            handler.startFetch(moviesFetch.getUpcomingMovies(apiKey, 1));
        }

        /**
         * get the movie detail
         * */
        public void getMovieDetails(int movieId,final View onCompleteShow, final View onDataFetchShow, final View onFailureShow,ShowHandler<MovieModel> handler){
            if(onCompleteShow!=null) {
                onCompleteShow.setVisibility(View.GONE);
            }
            if(onFailureShow!=null) {
                onFailureShow.setVisibility(View.GONE);
            }
            if(onDataFetchShow!=null) {
                onDataFetchShow.setVisibility(View.VISIBLE);
            }
            if(highOnShow.apiKey==null||highOnShow.apiKey.length()==0){
                if(onFailureShow!=null) {
                    onFailureShow.setVisibility(View.VISIBLE);
                }
                if(onDataFetchShow!=null) {
                    onDataFetchShow.setVisibility(View.GONE);
                }
                return;
            }
            handler.initViews(onCompleteShow,onDataFetchShow,onFailureShow);
            handler.startFetch(moviesFetch.getMovieDetailsById(movieId,apiKey));
        }

        public void getMovieCredits(int movieId,final View onCompleteShow, final View onDataFetchShow, final View onFailureShow,ShowHandler<CreditResponse> handler){

            if(onCompleteShow!=null) {
                onCompleteShow.setVisibility(View.GONE);
            }
            if(onFailureShow!=null) {
                onFailureShow.setVisibility(View.GONE);
            }
            if(onDataFetchShow!=null) {
                onDataFetchShow.setVisibility(View.VISIBLE);
            }

            if(highOnShow.apiKey==null||highOnShow.apiKey.length()==0){
                if(onFailureShow!=null) {
                    onFailureShow.setVisibility(View.VISIBLE);
                }
                if(onDataFetchShow!=null) {
                    onDataFetchShow.setVisibility(View.GONE);
                }
                return;
            }
            handler.initViews(onCompleteShow,onDataFetchShow,onFailureShow);
            handler.startFetch(moviesFetch.getMovieCreditsById(movieId,highOnShow.apiKey));
        }

        /**
         * get the movies matching a search keyword
         * */
        public void getMovieBySearch(String query,final View onCompleteShow, final View onDataFetchShow, final View onFailureShow,ShowHandler<MovieResponse> handler){
            if(onCompleteShow!=null) {
                onCompleteShow.setVisibility(View.GONE);
            }
            if(onFailureShow!=null) {
                onFailureShow.setVisibility(View.GONE);
            }
            if(onDataFetchShow!=null) {
                onDataFetchShow.setVisibility(View.VISIBLE);
            }

            if(apiKey==null||apiKey.length()==0){
                if(onFailureShow!=null) {
                    onFailureShow.setVisibility(View.VISIBLE);
                }
                if(onDataFetchShow!=null) {
                    onDataFetchShow.setVisibility(View.GONE);
                }
                return;
            }
            handler.initViews(onCompleteShow,onDataFetchShow,onFailureShow);
            handler.startFetch(moviesFetch.getMoviesBySearch(highOnShow.apiKey,query));
        }

    }

    public class TvShow{

        private TvShowFetch tvShowFetch;


        private HighOnShow highOnShow;

        public TvShow(HighOnShow highOnShow) {

            this.highOnShow = highOnShow;

            if (retrofit == null) {
                highOnShow.buildClient();
                tvShowFetch = highOnShow.retrofit.create(TvShowFetch.class);
            } else if (tvShowFetch==null) {
                tvShowFetch = highOnShow.retrofit.create(TvShowFetch.class);
            }
        }

        //todo:complete these methods
        public void getTvShowOnAir(final View onCompleteShow, final View onDataFetchShow, final View onFailureShow, ShowHandler<TvResponse> handler){
            if(onCompleteShow!=null) {
                onCompleteShow.setVisibility(View.GONE);
            }
            if(onFailureShow!=null) {
                onFailureShow.setVisibility(View.GONE);
            }
            if(onDataFetchShow!=null) {
                onDataFetchShow.setVisibility(View.VISIBLE);
            }

            if(highOnShow.apiKey==null||highOnShow.apiKey.length()==0){
                if(onFailureShow!=null) {
                    onFailureShow.setVisibility(View.VISIBLE);
                }
                if(onDataFetchShow!=null) {
                    onDataFetchShow.setVisibility(View.GONE);
                }
                return;
            }
            handler.initViews(onCompleteShow,onDataFetchShow,onFailureShow);
            handler.startFetch(tvShowFetch.getTvShowOnAir(highOnShow.apiKey,1));
        }

        public void getTvShowDetailsBySearch(String query,final View onCompleteShow, final View onDataFetchShow, final View onFailureShow,ShowHandler<TvResponse> handler){
            if(onCompleteShow!=null) {
                onCompleteShow.setVisibility(View.GONE);
            }
            if(onFailureShow!=null) {
                onFailureShow.setVisibility(View.GONE);
            }
            if(onDataFetchShow!=null) {
                onDataFetchShow.setVisibility(View.VISIBLE);
            }

            if(highOnShow.apiKey==null||highOnShow.apiKey.length()==0){
                if(onFailureShow!=null) {
                    onFailureShow.setVisibility(View.VISIBLE);
                }
                if(onDataFetchShow!=null) {
                    onDataFetchShow.setVisibility(View.GONE);
                }
                return;
            }
            handler.initViews(onCompleteShow,onDataFetchShow,onFailureShow);
            handler.startFetch(tvShowFetch.getTvShowBySearch(highOnShow.apiKey,query));
        }

        public void getTvShowDetailsById(int id, final View onCompleteShow, final View onDataFetchShow, final View onFailureShow,ShowHandler<TvShowModel> handler){
            if(onCompleteShow!=null) {
                onCompleteShow.setVisibility(View.GONE);
            }
            if(onFailureShow!=null) {
                onFailureShow.setVisibility(View.GONE);
            }
            if(onDataFetchShow!=null) {
                onDataFetchShow.setVisibility(View.VISIBLE);
            }

            if(highOnShow.apiKey==null||highOnShow.apiKey.length()==0){
                if(onFailureShow!=null) {
                    onFailureShow.setVisibility(View.VISIBLE);
                }
                if(onDataFetchShow!=null) {
                    onDataFetchShow.setVisibility(View.GONE);
                }
                return;
            }
            handler.initViews(onCompleteShow,onDataFetchShow,onFailureShow);
            handler.startFetch(tvShowFetch.getTvShowById(id,highOnShow.apiKey));
        }

        public void getTvShowSeasonsById(int id, int seasonNumber,final View onCompleteShow, final View onDataFetchShow, final View onFailureShow, ShowHandler<TvShowSeason> handler){
            if(onCompleteShow!=null) {
                onCompleteShow.setVisibility(View.GONE);
            }
            if(onFailureShow!=null) {
                onFailureShow.setVisibility(View.GONE);
            }
            if(onDataFetchShow!=null) {
                onDataFetchShow.setVisibility(View.VISIBLE);
            }

            if(highOnShow.apiKey==null||highOnShow.apiKey.length()==0){
                if(onFailureShow!=null) {
                    onFailureShow.setVisibility(View.VISIBLE);
                }
                if(onDataFetchShow!=null) {
                    onDataFetchShow.setVisibility(View.GONE);
                }
                return;
            }
            handler.initViews(onCompleteShow,onDataFetchShow,onFailureShow);
            handler.startFetch(tvShowFetch.getTvShowSeasonInfoById(id,seasonNumber,highOnShow.apiKey));
        }

    }

}
