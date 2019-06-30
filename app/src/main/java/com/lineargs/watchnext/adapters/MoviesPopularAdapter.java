package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Movies;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.util.List;

/**
 * Created by goranminov on 25/10/2017.
 * <p>
 * Extends our abstract BaseTabbedAdapter for better implementation
 */

public class MoviesPopularAdapter extends BaseTabbedAdapter {

    private List<Movies> movies;

    /**
     * Creates a MoviesPopularAdapter.
     * We call super as all our children views are being cached there.
     *
     * @param context  The context passed
     * @param listener The listener passed
     */
    public MoviesPopularAdapter(@NonNull Context context, @NonNull OnItemClickListener listener) {
        super(context, listener);
    }

    /**
     * Implemented bindViews method from our BaseTabbedAdapter
     * This is where we pass all the data from our Cursor.
     *
     * @param holder   Our TabbedViewHolder
     * @param context  The Context
     * @param position Adapter's position
     */
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

//        if (isFavorite(context, id)) {
//            holder.star.setImageDrawable(starImage());
//        } else {
//            holder.star.setImageDrawable(starImageBorder());
//        }
//
//        holder.star.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri uri = DataContract.PopularMovieEntry.buildMovieUriWithId(id);
//                if (isFavorite(context, id)) {
//                    DbUtils.removeFromFavorites(context, uri);
//                    Toast.makeText(context, context.getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
//                    holder.star.setImageDrawable(starImageBorder());
//                } else {
//                    DbUtils.addMovieToFavorites(context, uri);
//                    Toast.makeText(context, context.getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
//                    holder.star.setImageDrawable(starImage());
//                }
//            }
//        });
    }

    /**
     * Implemented onViewClick method from our BaseTabbedAdapter
     *
     * @param view     The View that has been clicked
     * @param position Adapter's position
     */
    @Override
    protected void onViewClick(View view, int position) {
        if (movies != null) {
            Movies currentMovie = movies.get(position);
            callback.onItemSelected(currentMovie.getTmdbId());
        }
    }

    /**
     * Returns the number of items to display.
     *
     * @return The number of items available.
     */
    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        } else {
            return movies.size();
        }
    }

    public void setPopularMovies(List<Movies> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}

