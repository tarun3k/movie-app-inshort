package com.tarun3k.movieapp.repo.local;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tarun3k.movieapp.model.Movie;

import java.util.List;

@Dao
public interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Nullable
    @Query("SELECT * FROM movie_table WHERE id =:id")
    Movie get(Integer id);

    @Query("DELETE FROM movie_table")
    void deleteAll();

    @Query("SELECT * FROM movie_table WHERE is_now_playing")
    LiveData<List<Movie>> getNowPlayingMovies();

    @Query("SELECT * FROM movie_table WHERE is_last_seen")
    LiveData<List<Movie>> getSeenMovies();

    @Query("SELECT * FROM movie_table WHERE is_trending ")
    LiveData<List<Movie>> getTrendingMovies();

    @Query("SELECT * FROM movie_table WHERE is_saved")
    LiveData<List<Movie>> getSavedMovies();
}
