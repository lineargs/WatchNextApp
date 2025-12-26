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
 * Created by goranminov on 04/11/2017.
 * <p>
 * See {@link MoviesPopularAdapter}
 */

public class SeriesOnTheAirAdapter extends BaseTabbedAdapter {

    private java.util.List<com.lineargs.watchnext.data.entity.OnTheAirSerie> series;

    public SeriesOnTheAirAdapter(@NonNull Context context, OnItemClickListener listener) {
        super(context, listener);
    }

    @Override
    protected void bindViews(final TabbedViewHolder holder, final Context context, int position) {
        final com.lineargs.watchnext.data.entity.OnTheAirSerie serie = series.get(position);
        final long id = serie.getTmdbId();
        if (isFavorite(context, id)) {
            holder.star.setImageDrawable(starImage());
        } else {
            holder.star.setImageDrawable(starImageBorder());
        }
        holder.title.setText(serie.getTitle());
        ServiceUtils.loadPicasso(holder.poster.getContext(), serie.getPosterPath())
                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                .centerCrop()
                .into(holder.poster);

        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = DataContract.OnTheAirSerieEntry.buildSerieUriWithId(id);
                if (isFavorite(context, id)) {
                    DbUtils.removeFromFavorites(context, uri);
                    Toast.makeText(context, context.getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
                    holder.star.setImageDrawable(starImageBorder());
                } else {
                    DbUtils.addTVToFavorites(context, uri);
                    Toast.makeText(context, context.getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
                    holder.star.setImageDrawable(starImage());
                }
            }
        });
    }

    @Override
    protected void onViewClick(View view, int position) {
        com.lineargs.watchnext.data.entity.OnTheAirSerie serie = series.get(position);
        Uri uri = DataContract.OnTheAirSerieEntry.buildSerieUriWithId(serie.getTmdbId());
        callback.onItemSelected(uri);
    }

    @Override
    public int getItemCount() {
        if (series == null) {
            return 0;
        } else {
            return series.size();
        }
    }

    public void swapSeries(java.util.List<com.lineargs.watchnext.data.entity.OnTheAirSerie> series) {
        this.series = series;
        notifyDataSetChanged();
    }
}



