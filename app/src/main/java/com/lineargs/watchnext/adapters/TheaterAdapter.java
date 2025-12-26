package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.dbutils.DbUtils;

/**
 * Created by goranminov on 17/10/2017.
 * <p>
 * See {@link MoviesPopularAdapter}
 */

public class TheaterAdapter extends BaseTabbedAdapter {

    private java.util.List<com.lineargs.watchnext.data.entity.TheaterMovie> movies;

    public TheaterAdapter(@NonNull Context context, OnItemClickListener listener) {
        super(context, listener);
    }

    @Override
    protected void bindViews(final TabbedViewHolder holder, final Context context, int position) {
        final com.lineargs.watchnext.data.entity.TheaterMovie movie = movies.get(position);
        final long id = movie.getTmdbId();
        if (isFavorite(context, id)) {
            holder.star.setImageDrawable(starImage());
        } else {
            holder.star.setImageDrawable(starImageBorder());
        }
        holder.title.setText(movie.getTitle());
        ServiceUtils.loadPicasso(holder.poster.getContext(), movie.getPosterPath())
                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                .centerCrop()
                .into(holder.poster);

        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = DataContract.TheaterMovieEntry.buildMovieUriWithId(id);
                boolean isFavorite = isFavorite(context, id);
                callback.onToggleFavorite(uri, isFavorite);
            }
        });
    }

    @Override
    protected void onViewClick(View view, int position) {
        com.lineargs.watchnext.data.entity.TheaterMovie movie = movies.get(position);
        Uri uri = DataContract.TheaterMovieEntry.buildMovieUriWithId(movie.getTmdbId());
        callback.onItemSelected(uri);
    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        } else {
            return movies.size();
        }
    }

    public void swapMovies(java.util.List<com.lineargs.watchnext.data.entity.TheaterMovie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
