package com.sbjr.project.fetch;

import com.sbjr.project.model.MovieModel;
import com.sbjr.project.response.MovieResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sbjr on 19/12/16.
 *
 * Interface to access Movie related apis
 */

public interface MoviesFetch {

    @GET("Movie/upcoming")
    MovieResponse getUpcomingMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movieid}")
    MovieModel getMovieDetailsById(@Query("api_key") String apiKey, @Path("{movieid") int movieid);

    @GET("search/movie")
    MovieResponse getMoviesBySearch(@Query("api_key")String apiKey,@Query(value = "query",encoded = true)String query);

}
