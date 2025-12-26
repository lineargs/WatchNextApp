package com.lineargs.watchnext.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;

import com.lineargs.watchnext.databinding.ItemMovieDetailBinding;

/**
 * Created by goranminov on 14/11/2017.
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private com.lineargs.watchnext.data.entity.Movie movie;

    public MovieDetailAdapter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieDetailBinding binding = ItemMovieDetailBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovieViewHolder viewHolder = (MovieViewHolder) holder;
        viewHolder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (movie == null) {
            return 0;
        } else {
            return 1;
        }
    }

    public void swapMovie(com.lineargs.watchnext.data.entity.Movie movie) {
        this.movie = movie;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        
        final ItemMovieDetailBinding binding;
        final AppCompatTextView title;
        final AppCompatTextView releaseDate;
        final AppCompatTextView overview;
        final AppCompatTextView voteAverage;
        final AppCompatTextView runtime;
        final AppCompatTextView companies;
        final AppCompatTextView countries;
        final AppCompatTextView genres;

        MovieViewHolder(ItemMovieDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.title = binding.title;
            this.releaseDate = binding.releaseDate;
            this.overview = binding.overview;
            this.voteAverage = binding.voteAverage;
            this.runtime = binding.runtime;
            this.companies = binding.companies;
            this.countries = binding.countries;
            this.genres = binding.genres;
        }

        void bindViews(int position) {
            title.setText(movie.getTitle());
            releaseDate.setText(movie.getReleaseDate());
            overview.setText(movie.getOverview());
            voteAverage.setText(movie.getVoteAverage());
            runtime.setText(context.getString(R.string.runtime, String.valueOf(movie.getRuntime())));
            companies.setText(movie.getProductionCompanies());
            countries.setText(movie.getProductionCountries());
            genres.setText(movie.getGenres());
        }
    }
}


