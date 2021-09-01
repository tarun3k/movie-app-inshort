package com.tarun3k.movieapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tarun3k.movieapp.databinding.LayoutGenreBinding;
import com.tarun3k.movieapp.model.SingleMovieData;

import java.util.List;

import javax.inject.Inject;

public class  MovieGenreAdapter extends RecyclerView.Adapter<MovieGenreAdapter.MovieGenreViewHolder> {

    @Inject
    public MovieGenreAdapter() {
        super();
    }

    List<SingleMovieData.Genres> list;
    @NonNull
    @Override
    public MovieGenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieGenreViewHolder(LayoutGenreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieGenreAdapter.MovieGenreViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ?0:list.size();
    }

    public void updateList(List<SingleMovieData.Genres> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class MovieGenreViewHolder extends RecyclerView.ViewHolder {
        LayoutGenreBinding genreBinding;
        public MovieGenreViewHolder(@NonNull LayoutGenreBinding binding) {
            super(binding.getRoot());
            genreBinding = binding;
        }

        public void bind(SingleMovieData.Genres genre) {
            genreBinding.textView.setText(genre.name);
            if(genre!=null && genre.name.length() ==0 ) {
                genreBinding.textView.setVisibility(View.GONE);
            } else {
                genreBinding.textView.setVisibility(View.VISIBLE);
            }
        }
    }
}
