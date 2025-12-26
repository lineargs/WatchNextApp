package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.entity.Favorites;
import com.lineargs.watchnext.databinding.ItemMainBinding;
import com.lineargs.watchnext.utils.ServiceUtils;

public class FavoritesAdapter extends ListAdapter<Favorites, FavoritesAdapter.FavoritesViewHolder> {

    private final Context context;
    private final OnItemClickListener itemClickListener;
    private final OnStarClickListener starClickListener;

    public FavoritesAdapter(Context context, OnItemClickListener itemClickListener, OnStarClickListener starClickListener) {
        super(new FavoritesDiffCallback());
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.starClickListener = starClickListener;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMainBinding binding = ItemMainBinding.inflate(LayoutInflater.from(context), parent, false);
        return new FavoritesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Favorites favorite = getItem(position);
        holder.bind(favorite);
    }

    public interface OnItemClickListener {
        void onItemSelected(Uri uri, boolean isSeries);
    }

    public interface OnStarClickListener {
        void onStarClicked(Favorites favorite);
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder {

        private final ItemMainBinding binding;

        FavoritesViewHolder(ItemMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            
            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Favorites favorite = getItem(position);
                    boolean isSeries = favorite.getType() == 1;
                    Uri uri = isSeries ? DataContract.Favorites.buildFavoritesUriWithId(favorite.getTmdbId()) : DataContract.Favorites.buildFavoritesUriWithId(favorite.getTmdbId());
                    // Actually, the Intent logic in MainActivity expects DataContract.Favorites.CONTENT_URI structure or similar?
                    // Original MainActivity:
                    // Uri uri = DataContract.Favorites.buildFavoritesUriWithId(Long.parseLong(cursor.getString(Query.ID)));
                    // But wait, the ID in cursor from MainAdapter was cursor.getInt(Query.ID) which is the TMDB ID?
                    // Let's check MainAdapter: 
                    // final long id = cursor.getInt(Query.ID);
                    // Uri uri = DataContract.PopularMovieEntry.buildMovieUriWithId(id); (This is for Star click)
                    // For Item click:
                    // Uri uri = DataContract.Favorites.buildFavoritesUriWithId(Long.parseLong(cursor.getString(Query.ID)));
                    
                    // In our case, we have the Favorites entity.
                    // Let's just pass the URI effectively.
                    // But wait, MainActivity logic is:
                    /*
                    if (isSeries(uri)) { ... } 
                    */
                    // The isSeries() helper checks the DB using the URI's last path segment (ID).
                    // So we should pass the ID.
                    
                    Uri itemUri = DataContract.Favorites.buildFavoritesUriWithId(favorite.getTmdbId());
                    itemClickListener.onItemSelected(itemUri, isSeries);
                }
            });

            binding.mainStarImage.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    starClickListener.onStarClicked(getItem(position));
                }
            });
        }

        void bind(Favorites favorite) {
            binding.mainTitle.setText(favorite.getTitle());
            binding.mainDate.setText(context.getString(R.string.main_date, favorite.getReleaseDate()));
            binding.mainVote.setText(favorite.getVoteAverage());
            
            if (favorite.getStatus() == null) {
                binding.mainStatus.setText(context.getString(R.string.main_status, context.getString(R.string.text_not_available)));
            } else {
                binding.mainStatus.setText(context.getString(R.string.main_status, favorite.getStatus()));
            }

            // Always a star since this is the Favorites list
            binding.mainStarImage.setImageDrawable(
                    VectorDrawableCompat.create(context.getResources(), R.drawable.icon_star_black, context.getTheme()));

            int placeholderId = favorite.getType() == 0 ? R.drawable.placeholder_movie : R.drawable.placeholder_serie;
            ServiceUtils.loadPicasso(context, favorite.getPosterPath())
                    .fit()
                    .placeholder(placeholderId)
                    .error(placeholderId)
                    .into(binding.mainPoster);
        }
    }

    static class FavoritesDiffCallback extends DiffUtil.ItemCallback<Favorites> {
        @Override
        public boolean areItemsTheSame(@NonNull Favorites oldItem, @NonNull Favorites newItem) {
            return oldItem.getTmdbId() == newItem.getTmdbId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Favorites oldItem, @NonNull Favorites newItem) {
            return oldItem.getTmdbId() == newItem.getTmdbId()
                    && java.util.Objects.equals(oldItem.getTitle(), newItem.getTitle())
                    && java.util.Objects.equals(oldItem.getVoteAverage(), newItem.getVoteAverage())
                    && java.util.Objects.equals(oldItem.getStatus(), newItem.getStatus())
                    && java.util.Objects.equals(oldItem.getReleaseDate(), newItem.getReleaseDate())
                    && java.util.Objects.equals(oldItem.getPosterPath(), newItem.getPosterPath())
                    && oldItem.getNotify() == newItem.getNotify();
        }
    }
}
