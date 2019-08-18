package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Movies;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 14/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Movies movie;

    public MovieDetailAdapter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_movie_detail, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovieViewHolder viewHolder = (MovieViewHolder) holder;
        viewHolder.bindViews();
    }

    @Override
    public int getItemCount() {
        if (movie == null) {
            return 0;
        } else {
            return 1;
        }
    }

    public void swapMovie(Movies movie) {
        this.movie = movie;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        AppCompatTextView title;
        @BindView(R.id.release_date)
        AppCompatTextView releaseDate;
        @BindView(R.id.overview)
        AppCompatTextView overview;
        @BindView(R.id.vote_average)
        AppCompatTextView voteAverage;
        @BindView(R.id.runtime)
        AppCompatTextView runtime;
        @BindView(R.id.companies)
        AppCompatTextView companies;
        @BindView(R.id.countries)
        AppCompatTextView countries;
        @BindView(R.id.genres)
        AppCompatTextView genres;

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindViews() {
            if (movie != null) {
                title.setText(movie.getTitle());
                releaseDate.setText(movie.getReleaseDate());
                overview.setText(movie.getOverview());
                voteAverage.setText(movie.getVoteAverage());
                runtime.setText(String.valueOf(movie.getRuntime()));
                companies.setText(movie.getProductionCompanies());
                countries.setText(movie.getProductionCountries());
                genres.setText(movie.getGenres());
            }
        }
    }
}


