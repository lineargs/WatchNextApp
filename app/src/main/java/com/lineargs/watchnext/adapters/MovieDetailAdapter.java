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

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_movie_detail, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
        }
    }
}


