package com.tarun3k.movieapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.tarun3k.movieapp.repo.local.GenreConverter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "single_movie_table")
public class SingleMovieData extends Movie {
    public class Genres{
        @ColumnInfo(defaultValue = "genre_id")
        @SerializedName("id")
        public Integer id;

        @ColumnInfo(defaultValue = "genre_name")
        @SerializedName("name")
        public String name;
    }

    @TypeConverters(GenreConverter.class)
    @SerializedName("genres")
    public List<Genres> listOfGenres;

    @SerializedName("overview")
    public String overview;
}



