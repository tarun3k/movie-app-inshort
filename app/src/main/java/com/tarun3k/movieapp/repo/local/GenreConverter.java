package com.tarun3k.movieapp.repo.local;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tarun3k.movieapp.model.SingleMovieData;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class GenreConverter {
    @TypeConverter
    public static List<SingleMovieData.Genres> storedStringToMyObjects(String genreString) {
        Gson gson = new Gson();
        if (genreString == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<SingleMovieData.Genres>>() {}.getType();
        return gson.fromJson(genreString, listType);
    }

    @TypeConverter
    public static String myObjectsToStoredString(List<SingleMovieData.Genres> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }
}
