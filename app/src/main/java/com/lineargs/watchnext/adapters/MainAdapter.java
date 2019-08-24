package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Favourites;
import com.squareup.picasso.Picasso;

import java.util.List;

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
    private List<Favourites> favourites;

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
        if (favourites == null) {
            return 0;
        }
        return favourites.size();
    }

    /**
     * Used to set the data on a Adapter if we've already
     * created one.
     *
     * @param favourites The new data to be displayed.
     */
    public void swapFavourites(List<Favourites> favourites) {
        this.favourites = favourites;
        notifyDataSetChanged();
    }

    private void bindMainViews(final MainViewHolder holder, final Context context, int position) {
        if (favourites != null) {
            Favourites favourite = favourites.get(position);
            holder.star.setImageDrawable(VectorDrawableCompat.create
                    (context.getResources(), R.drawable.icon_star_black, context.getTheme()));
//            holder.star.setOnClickListener(view -> {
//                Uri uri = DataContract.PopularMovieEntry.buildMovieUriWithId(id);
//                if (isFavorite(context, id)) {
//                    DbUtils.removeFromFavorites(context, uri);
//                    Toast.makeText(context, context.getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
//                    notifyDataSetChanged();
//                }
//            });
            holder.title.setText(favourite.getTitle());
            holder.date.setText(favourite.getReleaseDate());
            holder.vote.setText(favourite.getVoteAverage());
            if (favourite.getStatus() == null) {
                holder.status.setText(context.getString(R.string.main_status, context.getString(R.string.text_not_available)));
            } else {
                holder.status.setText(context.getString(R.string.main_status, favourite.getStatus()));
            }
            Picasso.with(holder.poster.getContext())
                    .load(favourite.getPosterPath())
                    .fit()
                    .into(holder.poster);
        }
    }

    public interface OnItemClickListener {
        void onItemSelected(String tmdbId);
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
            if (favourites != null) {
                Favourites favourite = favourites.get(getAdapterPosition());
                callback.onItemSelected(favourite.getImdbId());
            }
        }
    }
}
