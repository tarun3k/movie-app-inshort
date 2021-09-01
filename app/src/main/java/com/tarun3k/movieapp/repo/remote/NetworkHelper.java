package com.tarun3k.movieapp.repo.remote;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class NetworkHelper {
    private RemoteService remoteService;
    public static String BASE_URL = "https://api.themoviedb.org/3/";

    @Inject
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
