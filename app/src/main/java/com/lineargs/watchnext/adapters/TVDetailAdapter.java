package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.data.Series;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Series series;

    public TVDetailAdapter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_tv_detail, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovieViewHolder viewHolder = (MovieViewHolder) holder;
        viewHolder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (series == null) {
            return 0;
        } else {
            return 1;
        }
    }

    public void swapSeries(Series series) {
        this.series = series;
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
        @BindView(R.id.companies)
        AppCompatTextView companies;
        @BindView(R.id.genres)
        AppCompatTextView genres;

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindViews(int position) {
            if (series != null) {
                title.setText(series.getTitle());
                releaseDate.setText(series.getReleaseDate());
                overview.setText(series.getOverview());
                voteAverage.setText(series.getVoteAverage());
                companies.setText(series.getProductionCompanies());
                genres.setText(series.getGenres());
            }
        }
    }
}
