package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Movies;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.util.List;

/**
 * Created by goranminov on 17/10/2017.
 * <p>
 * Started refactoring on 29/09/2018.
 * <p>
 * <p>
 * Refactoring with View Model and Room data
 */

public class TheaterAdapter extends BaseTabbedAdapter {

    private List<Movies> movies;

    public TheaterAdapter(@NonNull Context context, OnItemClickListener listener) {
        super(context, listener);
    }

    @Override
    protected void bindViews(final TabbedViewHolder holder, final Context context, int position) {
        if (movies != null) {
            Movies currentMovie = movies.get(position);
            holder.title.setText(currentMovie.getTitle());
            //        if (isFavorite(context, id)) {
            holder.star.setImageDrawable(starImage());
//        } else {
//            holder.star.setImageDrawable(starImageBorder());
//        }
//        holder.title.setText(cursor.getString(Query.TITLE));
//
//        holder.star.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri uri = DataContract.TheaterMovieEntry.buildMovieUriWithId(id);
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
            ServiceUtils.loadPicasso(holder.poster.getContext(), currentMovie.getPosterPath())
                    .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                    .centerCrop()
                    .into(holder.poster);
        }
    }

    @Override
    protected void onViewClick(View view, int position) {
        if (movies != null) {
            Movies currentMovie = movies.get(position);
            Uri uri = DataContract.TheaterMovieEntry.buildMovieUriWithId(currentMovie.getId());
            callback.onItemSelected(uri);
        }
    }

    @Override
    public int getItemCount() {
        if (movies != null)
            return movies.size();
        else return 0;
    }

    public void setTheatreMovies(List<Movies> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
