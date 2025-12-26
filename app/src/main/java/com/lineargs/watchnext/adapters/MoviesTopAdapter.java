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
 * Created by goranminov on 25/10/2017.
 * <p>
 * See {@link MoviesPopularAdapter}
 */

public class MoviesTopAdapter extends BaseTabbedAdapter {

    private java.util.List<com.lineargs.watchnext.data.entity.TopRatedMovie> movies;


    public MoviesTopAdapter(@NonNull Context context, OnItemClickListener listener) {
        super(context, listener);
    }


    @Override
    protected void bindViews(final TabbedViewHolder holder, final Context context, int position) {
        final com.lineargs.watchnext.data.entity.TopRatedMovie movie = movies.get(position);
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
                Uri uri = DataContract.TopRatedMovieEntry.buildMovieUriWithId(id);
                if (isFavorite(context, id)) {
                    DbUtils.removeFromFavorites(context, uri);
                    Toast.makeText(context, context.getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
                    holder.star.setImageDrawable(starImageBorder());
                } else {
                    DbUtils.addMovieToFavorites(context, uri);
                    Toast.makeText(context, context.getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
                    holder.star.setImageDrawable(starImage());
                }
            }
        });
    }

    @Override
    protected void onViewClick(View view, int position) {
        com.lineargs.watchnext.data.entity.TopRatedMovie movie = movies.get(position);
        Uri uri = DataContract.TopRatedMovieEntry.buildMovieUriWithId(movie.getTmdbId());
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

    public void swapMovies(java.util.List<com.lineargs.watchnext.data.entity.TopRatedMovie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}

