package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
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
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mainView = LayoutInflater
                .from(context)
                .inflate(R.layout.item_main, parent, false);
        return new MainViewHolder(mainView);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindMainViews((MainViewHolder) holder, context, position);
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
        return cursor.getCount();
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
        if (cursor.getString(Query.STATUS) == null) {
            holder.status.setText(context.getString(R.string.main_status, context.getString(R.string.text_not_available)));
        } else {
            holder.status.setText(context.getString(R.string.main_status, cursor.getString(Query.STATUS)));
        }
        Picasso.get()
                .load(cursor.getString(Query.POSTER_PATH))
                .fit()
                .into(holder.poster);
    }

    public interface OnItemClickListener {
        void onItemSelected(Uri uri);
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
        @BindView(R.id.main_status)
        AppCompatTextView status;

        MainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (callback != null) {
                view.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            cursor.moveToPosition(getAdapterPosition());
            Uri uri = DataContract.Favorites.buildFavoritesUriWithId(
                    Long.parseLong(cursor.getString(Query.ID)));
            callback.onItemSelected(uri);
        }
    }
}
