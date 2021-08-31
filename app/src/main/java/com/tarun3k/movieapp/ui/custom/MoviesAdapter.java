package com.tarun3k.movieapp.ui.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tarun3k.movieapp.R;
import com.tarun3k.movieapp.databinding.ReccylerMovieItemBinding;
import com.tarun3k.movieapp.model.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    Callback callback;
    List<Movie> movies;
    public Boolean imageEnable= true;

    MoviesAdapter(Callback callback) {
        this.callback = callback;
    }
    MoviesAdapter() {
        this.callback = null;
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieViewHolder viewHolder =  new MovieViewHolder(
                ReccylerMovieItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false),
                position -> callback.onItemClick(movies.get(position).id));
        if(!imageEnable) {
            viewHolder.getBinding().image.setLayoutParams(new FrameLayout.LayoutParams(
                    (int) viewHolder.itemView.getResources().getDimension(R.dimen.search_image_width),
                    (int) viewHolder.itemView.getResources().getDimension(R.dimen.search_image_height)
                    ));
        }
        return viewHolder;
    }

    void updateList(List<Movie> list) {
        this.movies = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        if(movies == null) return 0;
        return movies.size();
    }

    public interface Callback {
        void onItemClick(Integer movieId);
    }


    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ReccylerMovieItemBinding binding;
        public MovieViewHolder(@NonNull  ReccylerMovieItemBinding binding, Callback callback) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view-> {
                if(callback!=null)
                callback.onViewHolderItemclick(getAdapterPosition());
            });
        }

        public  ReccylerMovieItemBinding getBinding() {
            return binding;
        }

        public void bind(Movie movie) {
            if(binding==null) return ;
            Glide.with(itemView).load(movie.imageUrl).placeholder(R.drawable.ic_launcher_background).into(binding.image);
            binding.title.setText(movie.title);
            binding.rating.setText(String.valueOf(movie.rating));
        }

        interface Callback {
            void onViewHolderItemclick(Integer position);
        }

    }
}
