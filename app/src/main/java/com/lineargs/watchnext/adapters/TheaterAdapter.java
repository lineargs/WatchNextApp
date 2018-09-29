package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Movies;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.dbutils.DbUtils;

import java.util.List;

/**
 * Created by goranminov on 17/10/2017.
 *
 * Started refactoring on 29/09/2018.
 * <p>
 *
 * Refactoring with View Model and Room data
 */

public class TheaterAdapter extends BaseTabbedAdapter {

    private List<Movies> movies;
//    private Cursor cursor;

    public TheaterAdapter(@NonNull Context context, OnItemClickListener listener) {
        super(context, listener);
    }

    @Override
    protected void bindViews(final TabbedViewHolder holder, final Context context, int position) {
        if (movies != null) {
            Movies currentMovie = movies.get(position);
            holder.title.setText(currentMovie.getTitle());
        }
//        cursor.moveToPosition(position);
//        final long id = cursor.getInt(Query.ID);
//        if (isFavorite(context, id)) {
//            holder.star.setImageDrawable(starImage());
//        } else {
//            holder.star.setImageDrawable(starImageBorder());
//        }
//        holder.title.setText(cursor.getString(Query.TITLE));
//        ServiceUtils.loadPicasso(holder.poster.getContext(), cursor.getString(Query.POSTER_PATH))
//                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
//                .centerCrop()
//                .into(holder.poster);
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
    }

    @Override
    protected void onViewClick(View view, int position) {
        if (movies != null) {
            Movies currentMovie = movies.get(position);
            Uri uri = DataContract.TheaterMovieEntry.buildMovieUriWithId(currentMovie.getId());
            callback.onItemSelected(uri);
        }
//        cursor.moveToPosition(position);
//        Uri uri = DataContract.TheaterMovieEntry.buildMovieUriWithId
//                (cursor.getInt(Query.ID));
//        callback.onItemSelected(uri);
    }

    @Override
    public int getItemCount() {
        if (movies != null)
            return movies.size();
        else return 0;
    }

//    public void swapCursor(Cursor cursor) {
//        this.cursor = cursor;
//        notifyDataSetChanged();
//    }

    public void setMovies(List<Movies> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
