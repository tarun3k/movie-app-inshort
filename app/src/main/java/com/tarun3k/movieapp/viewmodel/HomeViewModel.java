package com.tarun3k.movieapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tarun3k.movieapp.model.Movie;
import com.tarun3k.movieapp.model.SingleMovieData;
import com.tarun3k.movieapp.repo.MovieRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    MovieRepository movieRepository;

    public MutableLiveData<Boolean> openDetailsFragmentLiveData = new MutableLiveData<>();
    public HomeViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public LiveData<List<Movie>> observeTrendingMovies() {
        return movieRepository.trendingMoviesLiveData;
    }
    public  LiveData<Integer> observeNowPlayingTotalPagesLiveData() {
        return movieRepository.nowPlayingTotalPagesLiveData;
    }
    public LiveData<List<Movie>> observeNowPlayingMovies() {
        return movieRepository.nowPlayingMoviesLiveData;
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
        movieRepository.searchMoviesLiveData.setValue(null);
    }
}
