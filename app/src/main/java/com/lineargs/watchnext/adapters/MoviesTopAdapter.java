package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.movies.Movies;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.util.List;

/**
 * Created by goranminov on 25/10/2017.
 * <p>
 * See {@link MoviesPopularAdapter}
 */

public class MoviesTopAdapter extends BaseTabbedAdapter {

    private List<Movies> movies;


    public MoviesTopAdapter(@NonNull Context context, OnItemClickListener listener) {
        super(context, listener);
    }


    @Override
    protected void bindViews(final TabbedViewHolder holder, final Context context, int position) {
        if (movies != null) {
            Movies currentMovie = movies.get(position);
            holder.title.setText(currentMovie.getTitle());
            ServiceUtils.loadPicasso(holder.poster.getContext(), currentMovie.getPosterPath())
                    .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                    .centerCrop()
                    .into(holder.poster);
            holder.star.setImageDrawable(starImageBorder());
        }
    }

    @Override
    protected void onViewClick(View view, int position) {
        if (movies != null) {
            Movies currentMovie = movies.get(position);
            callback.onItemSelected(currentMovie.getTmdbId());
        }
    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        } else {
            return movies.size();
        }
    }

    public void setTopRatedMovies(List<Movies> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}

