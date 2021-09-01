package com.tarun3k.movieapp.viewmodel;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tarun3k.movieapp.model.Movie;
import com.tarun3k.movieapp.model.SingleMovieData;
import com.tarun3k.movieapp.repo.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HomeViewModel extends ViewModel {


    MovieRepository movieRepository;

    public MutableLiveData<Boolean> openDetailsFragmentLiveData = new MutableLiveData<>();

    private List<Pair<String,LiveData<List<Movie>>>> listOfWidgets = new ArrayList<>();

    @Inject
    public HomeViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void  intiAllLiveDatas() {
        listOfWidgets.add(new Pair("Saved Movies",movieRepository.getSavedMovies()));
        listOfWidgets.add(new Pair("Now Playing",movieRepository.nowPlayingMoviesLiveData));
        listOfWidgets.add(new Pair("Trending Movies",movieRepository.trendingMoviesLiveData));
        listOfWidgets.add(new Pair("Last Seen",movieRepository.getAllSeenMovies()));
    }

    public List<Pair<String,LiveData<List<Movie>>>> getList() {
        return listOfWidgets;
    }

    public LiveData<Integer> observeNowPlayingTotalPagesLiveData() {
        return movieRepository.nowPlayingTotalPagesLiveData;
    }

    public LiveData<SingleMovieData> observeCurrentMovieData() {
        return movieRepository.currentSelectedMovieLiveData;
    }

    public LiveData<List<Movie>> observeSearchMovies() {
        return movieRepository.searchMoviesLiveData;
    }

    public void fetchNowPlaying(Integer pageNo) {
        movieRepository.fetchNowPlaying(pageNo);
    }

    public void fetchData() {
        movieRepository.fetchNowPlaying(1);
        movieRepository.fetchTrending();
    }

    public void onMovieItemClick(Integer movieId) {
        movieRepository.fetchSingleMovie(movieId);
        setOpenDetailsFragmentLiveData(true);
    }

    public void setOpenDetailsFragmentLiveData(Boolean value) {
        openDetailsFragmentLiveData.setValue(value);
    }

    public void searchMovie(String text) {
        movieRepository.searchMovie(text);
    }

    public void resetSearchMovies() {
        ((MutableLiveData<List<Movie>>) movieRepository.searchMoviesLiveData).setValue(null);
    }

    public void saveMovieState(Integer movieId, boolean state) {
        movieRepository.saveMovieState(movieId, state);
    }
}
