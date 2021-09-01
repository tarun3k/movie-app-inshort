package com.tarun3k.movieapp.repo.remote;


import com.tarun3k.movieapp.model.SearchResponse;
import com.tarun3k.movieapp.model.SingleMovieData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemoteService {
    String API_KEY = "api_key=44be25dca5ed7d023c137cb2427c2af5";
    @GET("/3/movie/now_playing?"+API_KEY)
    Call<SearchResponse> getNowPlaying(@Query("page") Integer pageNo);

    @GET("/3/trending/{media_type}/{time_window}?"+API_KEY)
    Call<SearchResponse> getTrending(@Path("media_type") String mediaType,
                                     @Path("time_window") String timeWindow);

    @GET("/3/movie/{movie_id}?"+API_KEY)
    Call<SingleMovieData> getMovieById(@Path("movie_id") Integer movieId);

    @GET("/3/search/movie?"+API_KEY)
    Call<SearchResponse> searchMovie(@Query("query") String query);
}

