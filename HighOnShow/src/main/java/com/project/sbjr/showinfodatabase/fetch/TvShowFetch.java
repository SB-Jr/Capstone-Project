package com.project.sbjr.showinfodatabase.fetch;

import com.project.sbjr.showinfodatabase.model.TvShowModel;
import com.project.sbjr.showinfodatabase.model.TvShowSeason;
import com.project.sbjr.showinfodatabase.response.TvResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sbjr on 19/12/16.
 */

public interface TvShowFetch {

    @GET("tv/on_the_air")
    Call<TvResponse> getTvShowOnAir(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/{id}")
    Call<TvShowModel> getTvShowById(@Path("id")int tvShowId,@Query("api_key")String apiKey);

    @GET("tv/{id}/season/{ses_num}")
    Call<TvShowSeason> getTvShowSeasonInfoById(@Path("id")int tvShowId,@Path("ses_num")int season_number,@Query("api_key")String apiKey);

    @GET("search/tv")
    Call<TvResponse> getTvShowBySearch(@Query("api_key")String apiKey, @Query(value = "query", encoded = true)String query);

}
