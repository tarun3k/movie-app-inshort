package com.tarun3k.movieapp.repo.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {
    private RemoteService remoteService;
    public static String BASE_URL = "https://api.themoviedb.org/3/";

    public NetworkHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        remoteService = retrofit.create(RemoteService.class);
    }

    public RemoteService getService() {
        return remoteService;
    }
}
