package com.sbjr.project.fetch;

import com.sbjr.project.response.UpcomingResponse;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sbjr on 19/12/16.
 *
 * Interface to access Movie related apis
 */

public interface MoviesFetch {

    @GET("movie/upcoming")
    UpcomingResponse getUpcomingMovies(@Query("api_key") String apiKey,@Query("page") int page);



}
