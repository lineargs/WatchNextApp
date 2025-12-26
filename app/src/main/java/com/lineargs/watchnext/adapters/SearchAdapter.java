package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.sync.syncsearch.SearchSyncUtils;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.dbutils.DbUtils;

import com.lineargs.watchnext.databinding.ItemSearchBinding;

/**
 * Created by goranminov on 06/11/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private java.util.List<com.lineargs.watchnext.data.entity.Search> searchResults;
    private Context context;

    public SearchAdapter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchBinding binding = ItemSearchBinding.inflate(LayoutInflater.from(context), parent, false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchViewHolder searchViewHolder = (SearchViewHolder) holder;
        searchViewHolder.bindViews(context, position);
    }

    @Override
    public int getItemCount() {
        if (searchResults == null) {
            return 0;
        }
        return searchResults.size();
    }

    public void swapSearchResults(java.util.List<com.lineargs.watchnext.data.entity.Search> searchResults) {
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    public boolean isFavorite(Context context, long id) {
        Uri uri = DataContract.Favorites.buildFavoritesUriWithId(id);
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private VectorDrawableCompat addToFavorites(Context context) {
        return VectorDrawableCompat.create(context.getResources(), R.drawable.icon_add_black, context.getTheme());
    }

    private VectorDrawableCompat deleteFromFavorites(Context context) {
        return VectorDrawableCompat.create(context.getResources(), R.drawable.icon_delete_black, context.getTheme());
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        final ItemSearchBinding binding;
        final ImageView poster;
        final ImageView star;
        final TextView title;

        SearchViewHolder(ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.poster = binding.mainPoster;
            this.star = binding.starImage;
            this.title = binding.mainTitle;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                com.lineargs.watchnext.data.entity.Search search = searchResults.get(position);
                int tmdbId = search.getTmdbId();
                if (search.getMediaType() == 0) {
                    Intent intent = new Intent(context, com.lineargs.watchnext.ui.MovieDetailsActivity.class);
                    intent.setData(DataContract.Search.buildSearchUriWithId(tmdbId));
                    context.startActivity(intent);
                } else if (search.getMediaType() == 1) {
                    Intent intent = new Intent(context, com.lineargs.watchnext.ui.SeriesDetailsActivity.class);
                    // We pass the Search URI for consistency with standard loaders but add ID as extra
                    // SeriesDetailsActivity logic needs to be verified to handle this if it relies on distinct tables
                    intent.setData(DataContract.Search.buildSearchUriWithId(tmdbId)); 
                    context.startActivity(intent);
                }
            }
        }

        void bindViews(final Context context, int position) {
            final com.lineargs.watchnext.data.entity.Search search = searchResults.get(position);
            final int tmdbId = search.getTmdbId(); 
            
            if (isFavorite(context, tmdbId)) {
                star.setImageDrawable(deleteFromFavorites(context));
            } else {
                star.setImageDrawable(addToFavorites(context));
            }
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFavorite(context, tmdbId)) {
                        DbUtils.removeFromFavorites(context, DataContract.Search.buildSearchUriWithId(tmdbId));
                        Toast.makeText(context, context.getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
                        star.setImageDrawable(addToFavorites(context));
                    } else {
                        if (search.getMediaType() == 0) {
                            SearchSyncUtils.syncSearchMovie(context, String.valueOf(tmdbId));
                        } else {
                            SearchSyncUtils.syncTV(context, String.valueOf(tmdbId));
                        }
                        Toast.makeText(context, context.getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
                        star.setImageDrawable(deleteFromFavorites(context));
                    }
                }
            });
            title.setText(search.getTitle());
            ServiceUtils.loadPicasso(poster.getContext(), search.getPosterPath())
                    .centerInside()
                    .fit()
                    .into(poster);
            
            // Visual indicator
            if (binding.mediaType != null) {
                if (search.getMediaType() == 0) {
                    binding.mediaType.setText(context.getString(R.string.movie));
                } else {
                    binding.mediaType.setText(context.getString(R.string.tv_series));
                }
            }
        }
    }
}

