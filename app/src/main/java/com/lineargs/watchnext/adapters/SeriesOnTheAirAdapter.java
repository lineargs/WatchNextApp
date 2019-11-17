package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.series.Series;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.util.List;

/**
 * Created by goranminov on 04/11/2017.
 * <p>
 * See {@link MoviesPopularAdapter}
 */

public class SeriesOnTheAirAdapter extends BaseTabbedAdapter {

    private List<Series> series;

    public SeriesOnTheAirAdapter(@NonNull Context context, OnItemClickListener listener) {
        super(context, listener);
    }

    @Override
    protected void bindViews(final TabbedViewHolder holder, final Context context, int position) {
        if (series != null) {
            Series currentSeries = series.get(position);
            holder.title.setText(currentSeries.getTitle());
            holder.star.setImageDrawable(starImage());
            ServiceUtils.loadPicasso(holder.poster.getContext(), currentSeries.getPosterPath())
                    .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                    .centerCrop()
                    .into(holder.poster);
        }
//        if (isFavorite(context, id)) {
//            holder.star.setImageDrawable(starImage());
//        } else {
//            holder.star.setImageDrawable(starImageBorder());
//        }
//        holder.star.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri uri = DataContract.OnTheAirSerieEntry.buildSerieUriWithId(id);
//                if (isFavorite(context, id)) {
//                    DbUtils.removeFromFavorites(context, uri);
//                    Toast.makeText(context, context.getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
//                    holder.star.setImageDrawable(starImageBorder());
//                } else {
//                    DbUtils.addTVToFavorites(context, uri);
//                    Toast.makeText(context, context.getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
//                    holder.star.setImageDrawable(starImage());
//                }
//            }
//        });
    }

    @Override
    protected void onViewClick(View view, int position) {
        if (series != null) {
            Series serie = series.get(position);
            callback.onItemSelected(serie.getTmdbId());
        }
    }

    @Override
    public int getItemCount() {
        if (series == null) {
            return 0;
        } else {
            return series.size();
        }
    }

    public void setOnTheAirSeries(List<Series> onTheAirSeries) {
        this.series = onTheAirSeries;
        notifyDataSetChanged();
    }
}



