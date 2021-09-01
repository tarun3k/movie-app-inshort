package com.tarun3k.movieapp.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tarun3k.movieapp.MainActivity;
import com.tarun3k.movieapp.R;
import com.tarun3k.movieapp.databinding.FragmentMovieDetailsBinding;
import com.tarun3k.movieapp.ui.adapters.MovieGenreAdapter;
import com.tarun3k.movieapp.ui.base.BaseFragment;
import com.tarun3k.movieapp.viewmodel.HomeViewModel;

import javax.inject.Inject;

public class MovieDetailsFragment extends BaseFragment {

    public static String SCREEN_NAME = "MovieDetailsFragment";
    public static String MOVIE_DATA = "movie_data";

    private FragmentMovieDetailsBinding binding;

    @Inject
    public HomeViewModel viewModel;

    @Inject
    public MovieGenreAdapter genereAdapter;
    private Boolean saveIconState=false;

    @Override
    public String getScreenName() {
        return SCREEN_NAME;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) requireActivity()).appComponent.inject(this);
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
        binding.saveIcon.setVisibility(View.GONE);
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
                binding.saveIcon.setVisibility(View.VISIBLE);
                binding.desc.setText(singleMovieData.overview);
                setIcon(singleMovieData.isSaved);
                binding.saveIcon.setOnClickListener(view-> {
                    saveIconState = !saveIconState;
                    setIcon(saveIconState);
                    viewModel.saveMovieState(singleMovieData.id, saveIconState);
                });
            }
            stopProgressDialog();
        });
    }


    private void setIcon(boolean isSaved) {
        saveIconState= isSaved;
        binding.saveIcon.setBackground(
                ResourcesCompat.getDrawable(getResources(), saveIconState ? R.drawable.ic_like_icon_selected:R.drawable.ic_like_icon_deselected, null));
    }
}
