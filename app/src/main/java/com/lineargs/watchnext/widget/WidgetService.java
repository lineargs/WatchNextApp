package com.lineargs.watchnext.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.io.IOException;

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

    private class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        Cursor mCursor;

        ListRemoteViewsFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        //called on start and when notifyAppWidgetViewDataChanged is called
        @Override
        public void onDataSetChanged() {
            if (mCursor != null) mCursor.close();
            mCursor = mContext.getContentResolver().query(
                    DataContract.Favorites.CONTENT_URI,
                    Query.PROJECTION,
                    null,
                    null,
                    null);
        }

        @Override
        public void onDestroy() {
            if (mCursor != null) mCursor.close();
        }

        @Override
        public int getCount() {
            if (mCursor == null) return 0;
            return mCursor.getCount();
        }

        /**
         * This method acts like the onBindViewHolder method in an Adapter
         *
         * @param i The current position of the item in the ListView to be displayed
         * @return The RemoteViews object to display for the provided position
         */
        @Override
        public RemoteViews getViewAt(int i) {
            if (mCursor == null || mCursor.getCount() == 0) return null;

            mCursor.moveToPosition(i);

            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_app);

            Intent fillIntent = new Intent();

            fillIntent.setData(DataContract.Favorites.CONTENT_URI);

            views.setOnClickFillInIntent(R.id.widget_row, fillIntent);

            views.setTextViewText(R.id.widget_title, mCursor.getString(Query.TITLE));

            String posterPath = mCursor.getString(Query.POSTER_PATH);

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
