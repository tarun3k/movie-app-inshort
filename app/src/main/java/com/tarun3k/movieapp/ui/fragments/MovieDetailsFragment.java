package com.tarun3k.movieapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tarun3k.movieapp.databinding.FragmentMovieDetailsBinding;
import com.tarun3k.movieapp.ui.adapters.MovieGenreAdapter;
import com.tarun3k.movieapp.ui.base.BaseFragment;
import com.tarun3k.movieapp.viewmodel.HomeViewModel;

public class MovieDetailsFragment extends BaseFragment {

    public static String SCREEN_NAME = "MovieDetailsFragment";
    public static String MOVIE_DATA = "movie_data";

    private FragmentMovieDetailsBinding binding;
    private HomeViewModel viewModel;
    private MovieGenreAdapter genereAdapter;

    @Override
    public String getScreenName() {
        return SCREEN_NAME;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(mActivity).get(HomeViewModel.class);
    }

    private void finish() {
        if(isAlive()) {
            mActivity.onBackPressed();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMovieDetailsBinding.inflate(getLayoutInflater());
        intiViews();
        initWithMovie();
        return binding.getRoot();
    }

    private void intiViews() {
        binding.separator.setVisibility(View.GONE);
        genereAdapter = new MovieGenreAdapter();
        binding.rcGenre.setAdapter(genereAdapter);
        binding.rcGenre.setLayoutManager(new LinearLayoutManager(binding.rcGenre.getContext(), RecyclerView.HORIZONTAL, false));
        startProgressDialog();
    }

    private void startProgressDialog() {
        binding.progressBar.setIndeterminate(true);
        binding.progressBar.setVisibility(View.VISIBLE);
    }
    private void stopProgressDialog() {
        binding.progressBar.setIndeterminate(false);
        binding.progressBar.setVisibility(View.GONE);
    }
    private void initWithMovie() {
        if(viewModel == null) return ;
        viewModel.observeCurrentMovieData().observe(getViewLifecycleOwner(), singleMovieData -> {
            if(singleMovieData!=null) {
                genereAdapter.updateList(singleMovieData.listOfGenres);
                Glide.with(binding.getRoot()).load(singleMovieData.imageUrl).into(binding.image);
                binding.title.setText(singleMovieData.title);
                binding.separator.setVisibility(View.VISIBLE);
                binding.desc.setText(singleMovieData.overview);
            }
            stopProgressDialog();
        });
    }
}
