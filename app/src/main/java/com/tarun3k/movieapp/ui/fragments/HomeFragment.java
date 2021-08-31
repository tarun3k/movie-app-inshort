package com.tarun3k.movieapp.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.tarun3k.movieapp.databinding.FragmentHomeBinding;
import com.tarun3k.movieapp.repo.MovieRepository;
import com.tarun3k.movieapp.repo.remote.NetworkHelper;
import com.tarun3k.movieapp.ui.base.BaseFragment;
import com.tarun3k.movieapp.ui.custom.PaginationListener;
import com.tarun3k.movieapp.viewmodel.HomeViewModel;


public class HomeFragment extends BaseFragment {

    public static String SCREEN_NAME = "HomeFragment";

    private FragmentHomeBinding binding;

    private HomeViewModel viewModel;


    @Override
    public String getScreenName() {
        return SCREEN_NAME;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(mActivity).get(HomeViewModel.class);
        viewModel.fetchData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        intiViews();
        intiObservers();
        attachCallback();
        return binding.getRoot();
    }


    private void intiViews() {
        binding.searchItems.disableTitle();
        binding.searchItems.disableMoviesImage();
        binding.searchContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.searchMovie(editable.toString());
                if(editable.length() ==0) {
                    viewModel.resetSearchMovies();
                }
            }
        });
    }

    private void attachCallback() {
        binding.nowPlaying.setCallback((movieId)->{
                viewModel.onMovieItemClick(movieId);
        });
        binding.trending.setCallback( (movieId -> {
            viewModel.onMovieItemClick(movieId);
        }));
        binding.searchItems.setCallback((movieId -> {
            viewModel.onMovieItemClick(movieId);
        }));
    }

    private void enableNowPlayingPagination(Integer totalNumber) {
        binding.nowPlaying.enablePagination(totalNumber, new PaginationListener() {
            @Override
            public void onNextPage(Integer pageNo) {
                viewModel.fetchNowPlaying(pageNo);
            }

            @Override
            public void onPaginationFinished() {
                //
            }
        });
    }

    private void intiObservers() {
        viewModel.observeNowPlayingMovies().observe(getViewLifecycleOwner(), nowPlayingMovieList -> {
            binding.nowPlaying.updateView("Now Playing", nowPlayingMovieList);
        });

        viewModel.observeTrendingMovies().observe(getViewLifecycleOwner(), trendingMovieList -> {
            binding.trending.updateView("Trending", trendingMovieList);
        });
        viewModel.observeNowPlayingTotalPagesLiveData().observe(getViewLifecycleOwner(), this::enableNowPlayingPagination);
        viewModel.observeSearchMovies().observe(getViewLifecycleOwner(), movies -> {
            binding.searchItems.updateView("", movies);
        });
    }
}
