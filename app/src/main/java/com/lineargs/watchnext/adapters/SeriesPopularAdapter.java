package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Series;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.util.List;

/**
 * Created by goranminov on 03/11/2017.
 * <p>
 * See {@link MoviesPopularAdapter}
 */

public class SeriesPopularAdapter extends BaseTabbedAdapter {

    private List<Series> series;

    public SeriesPopularAdapter(@NonNull Context context, OnItemClickListener listener) {
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

    public void setPopularSeries(List<Series> series) {
        this.series = series;
        notifyDataSetChanged();
    }
}

