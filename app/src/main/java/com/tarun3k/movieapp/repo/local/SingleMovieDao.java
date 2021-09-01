package com.tarun3k.movieapp.repo.local;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tarun3k.movieapp.model.Movie;
import com.tarun3k.movieapp.model.SingleMovieData;

import java.util.List;

@Dao
public interface SingleMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SingleMovieData movie);

    @Nullable
    @Query("SELECT * FROM single_movie_table WHERE id =:id")
    SingleMovieData get(Integer id);

    @Query("SELECT * FROM single_movie_table WHERE is_saved")
    LiveData<List<Movie>> getSavedMovies();


    @Query("SELECT * FROM single_movie_table WHERE is_last_seen")
    LiveData<List<Movie>> getLastSeen();

    @Query("DELETE FROM single_movie_table")
    void deleteAll();
}
