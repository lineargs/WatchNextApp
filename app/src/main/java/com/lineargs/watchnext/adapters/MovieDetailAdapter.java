package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Query;

import com.lineargs.watchnext.databinding.ItemMovieDetailBinding;

/**
 * Created by goranminov on 14/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Cursor cursor;

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
        if (cursor == null) {
            return 0;
        } else {
            return cursor.getCount();
        }
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
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
            cursor.moveToPosition(position);
            title.setText(cursor.getString(Query.TITLE));
            releaseDate.setText(cursor.getString(Query.RELEASE_DATE));
            overview.setText(cursor.getString(Query.OVERVIEW));
            voteAverage.setText(cursor.getString(Query.VOTE_AVERAGE));
            runtime.setText(context.getString(R.string.runtime, cursor.getString(Query.RUNTIME)));
            companies.setText(cursor.getString(Query.PRODUCTION_COMPANIES));
            countries.setText(cursor.getString(Query.PRODUCTION_COUNTRIES));
            genres.setText(cursor.getString(Query.GENRES));
        }
    }
}


