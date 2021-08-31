package com.tarun3k.movieapp.repo.remote;


import com.tarun3k.movieapp.model.SearchResponse;
import com.tarun3k.movieapp.model.SingleMovieData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemoteService {

    @GET("/3/movie/now_playing")
    Call<SearchResponse> getNowPlaying(@Query("pageNo") Integer pageNo, @Query("api_key") String apiKey);

    @GET("/3/trending/{media_type}/{time_window}")
    Call<SearchResponse> getTrending(@Path("media_type") String mediaType,
                                     @Path("time_window") String timeWindow, @Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}")
    Call<SingleMovieData> getMovieById(@Path("movie_id") Integer movieId, @Query("api_key") String apiKey);

    @GET("/3/search/movie")
    Call<SearchResponse> searchMovie(@Query("query") String query, @Query("api_key") String apiKey);




}

