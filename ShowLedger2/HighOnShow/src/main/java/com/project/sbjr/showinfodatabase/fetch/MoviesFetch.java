package com.project.sbjr.showinfodatabase.fetch;


import com.project.sbjr.showinfodatabase.model.MovieModel;
import com.project.sbjr.showinfodatabase.response.CreditResponse;
import com.project.sbjr.showinfodatabase.response.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sbjr on 19/12/16.
 *
 * Interface to access Movie related apis
 */

public interface MoviesFetch {

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{id}")
    Call<MovieModel> getMovieDetailsById(@Path("id") int movieid,@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResponse> getMoviesBySearch(@Query("api_key") String apiKey, @Query(value = "query", encoded = true) String query);

    @GET("movie/{id}/credits")
    Call<CreditResponse> getMovieCreditsById(@Path("id") int movieId,@Query("api_key") String apiKey);

}
