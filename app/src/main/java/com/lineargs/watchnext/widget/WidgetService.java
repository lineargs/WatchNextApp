package com.lineargs.watchnext.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.WatchNextDatabase;
import com.lineargs.watchnext.data.entity.Favorites;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by goranminov on 24/11/2017.
 * <p>
 * See {@link RemoteViewsService}
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    private static class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        List<Favorites> mFavorites;

        ListRemoteViewsFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        //called on start and when notifyAppWidgetViewDataChanged is called
        @Override
        public void onDataSetChanged() {
            mFavorites = WatchNextDatabase.getDatabase(mContext).favoritesDao().getFavoritesSync();
        }

        @Override
        public void onDestroy() {
            if (mFavorites != null) {
                mFavorites.clear();
            }
        }

        @Override
        public int getCount() {
            if (mFavorites == null) return 0;
            return mFavorites.size();
        }

        /**
         * This method acts like the onBindViewHolder method in an Adapter
         *
         * @param i The current position of the item in the ListView to be displayed
         * @return The RemoteViews object to display for the provided position
         */
        @Override
        public RemoteViews getViewAt(int i) {
            if (mFavorites == null || mFavorites.size() == 0) return null;

            Favorites favorite = mFavorites.get(i);

            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_app);

            Intent fillIntent = new Intent();
            android.net.Uri uri = com.lineargs.watchnext.data.DataContract.Favorites.buildFavoritesUriWithId(favorite.getTmdbId());
            fillIntent.setData(uri);
            fillIntent.putExtra("is_series", favorite.getType() == 1);
            views.setOnClickFillInIntent(R.id.widget_row, fillIntent);

            views.setTextViewText(R.id.widget_title, favorite.getTitle());

            String posterPath = favorite.getPosterPath();

            setWidgetPoster(views, posterPath);

            return views;
        }

        private void setWidgetPoster(RemoteViews views, String posterPath) {
            Bitmap bitmap;
            try {
                bitmap = ServiceUtils.loadPicasso(mContext, posterPath)
                        .centerCrop()
                        .resizeDimen(R.dimen.movie_poster_small_width, R.dimen.movie_poster_small_height)
                        .get();
            } catch (IOException e) {
                bitmap = null;
            }
            if (bitmap != null) {
                views.setImageViewBitmap(R.id.widget_poster, bitmap);
            } else {
                views.setImageViewResource(R.id.widget_poster, R.drawable.icon_star_white);
            }
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
