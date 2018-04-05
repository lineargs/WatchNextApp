package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.utils.dbutils.DbUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 14/10/2017.
 * <p>
 * Simple RecyclerView Adapter
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_SORT = 0;
    private final int VIEW_TYPE_MAIN = 1;
    private OnItemClickListener callback;
    private Context context;
    private Cursor cursor;

    /**
     * Creates a MainAdapter.
     */
    public MainAdapter(@NonNull Context context, @NonNull OnItemClickListener clickListener) {
        this.context = context;
        callback = clickListener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
     * @param viewType If the RecyclerView has more than one type of item.
     * @return A new MainViewHolder that holds the View for each grid item
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_SORT:
                View sortView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.item_sort, parent, false);
                return new SortViewHolder(sortView);
            case VIEW_TYPE_MAIN:
                View mainView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.item_main, parent, false);
                return new MainViewHolder(mainView);
            default:
                throw new IllegalArgumentException("Invalid view type, value of" + viewType);
        }
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_SORT:
                bindSortView((SortViewHolder) holder);
                break;
            case VIEW_TYPE_MAIN:
                bindMainViews((MainViewHolder) holder, context, position - 1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_SORT;
        } else {
            return VIEW_TYPE_MAIN;
        }
    }

    /**
     * Returns the number of items to display.
     *
     * @return The number of items available.
     */
    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return 1 + cursor.getCount();
    }

    /**
     * Used to set the data on a Adapter if we've already
     * created one.
     *
     * @param cursor The new data to be displayed.
     */
    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    /*
     * Used to check if the data contains
     * in the favorites table before we display it
     */
    private boolean isFavorite(Context context, long id) {
        Uri uri = DataContract.Favorites.buildFavoritesUriWithId(id);
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if (cursor == null) {
            return false;
        }
        boolean favorite = cursor.getCount() > 0;
        cursor.close();
        return favorite;
    }

    private void bindSortView(final SortViewHolder holder) {
    }

    private void bindMainViews(final MainViewHolder holder, final Context context, int position) {
        cursor.moveToPosition(position);
        final long id = cursor.getInt(Query.ID);

        holder.star.setImageDrawable(VectorDrawableCompat.create
                (context.getResources(), R.drawable.icon_star_black, context.getTheme()));
        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = DataContract.PopularMovieEntry.buildMovieUriWithId(id);
                if (isFavorite(context, id)) {
                    DbUtils.removeFromFavorites(context, uri);
                    Toast.makeText(context, context.getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });
        holder.title.setText(cursor.getString(Query.TITLE));
        holder.date.setText(context.getString(R.string.main_date, cursor.getString(Query.RELEASE_DATE)));
        holder.vote.setText(cursor.getString(Query.VOTE_AVERAGE));
        Picasso.with(holder.poster.getContext())
                .load(cursor.getString(Query.POSTER_PATH))
                .fit()
                .into(holder.poster);
    }

    public interface OnItemClickListener {
        void onItemSelected(Uri uri);

        void onSortClick();
    }

    /*
     * Cache of the children views.
     */
    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.main_poster)
        ImageView poster;
        @BindView(R.id.main_title)
        AppCompatTextView title;
        @BindView(R.id.main_date)
        TextView date;
        @BindView(R.id.main_vote)
        AppCompatTextView vote;
        @BindView(R.id.main_star_image)
        ImageView star;

        MainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (callback != null) {
                view.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            cursor.moveToPosition(getAdapterPosition() - 1);
            Uri uri = DataContract.Favorites.buildFavoritesUriWithId(
                    Long.parseLong(cursor.getString(Query.ID)));
            callback.onItemSelected(uri);
        }
    }

    class SortViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.main_sort)
        AppCompatTextView sort;

        SortViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (callback != null) {
                view.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            callback.onSortClick();
        }
    }
}
