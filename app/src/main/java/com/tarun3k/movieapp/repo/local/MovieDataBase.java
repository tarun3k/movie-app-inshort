package com.tarun3k.movieapp.repo.local;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tarun3k.movieapp.model.Movie;
import com.tarun3k.movieapp.model.SingleMovieData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Movie.class, SingleMovieData.class}, version = 1, exportSchema = false)
public abstract class MovieDataBase extends RoomDatabase {

    public abstract MoviesDao movieDao();
    public abstract SingleMovieDao singleMovieDao();

    private static volatile MovieDataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MovieDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDataBase.class, "movie_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

