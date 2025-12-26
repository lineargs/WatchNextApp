package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.content.Intent;
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

import com.lineargs.watchnext.databinding.ItemSearchBinding;

/**
 * Created by goranminov on 06/11/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnSearchItemClickListener {
        void onSearchItemSelected(int tmdbId, int mediaType);
        void onToggleFavorite(int tmdbId, int mediaType, boolean isFavorite);
    }
    
    private OnSearchItemClickListener callback;
    private java.util.List<com.lineargs.watchnext.data.entity.Search> searchResults;
    private Context context;

    public SearchAdapter(@NonNull Context context, OnSearchItemClickListener listener) {
        this.context = context;
        this.callback = listener;
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

    private java.util.Set<Integer> favoriteMovieIds = new java.util.HashSet<>();
    private java.util.Set<Integer> favoriteSeriesIds = new java.util.HashSet<>();

    public void setFavoriteMovieIds(java.util.List<Integer> ids) {
        this.favoriteMovieIds = new java.util.HashSet<>(ids);
        notifyDataSetChanged();
    }

    public void setFavoriteSeriesIds(java.util.List<Integer> ids) {
        this.favoriteSeriesIds = new java.util.HashSet<>(ids);
        notifyDataSetChanged();
    }
    
    private boolean isFavorite(int id, int mediaType) {
        if (mediaType == 0) {
            return favoriteMovieIds.contains(id);
        } else {
            return favoriteSeriesIds.contains(id);
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
                callback.onSearchItemSelected(search.getTmdbId(), search.getMediaType());
            }
        }

        void bindViews(final Context context, int position) {
            final com.lineargs.watchnext.data.entity.Search search = searchResults.get(position);
            final int tmdbId = search.getTmdbId(); 
            final int mediaType = search.getMediaType();
            
            if (isFavorite(tmdbId, mediaType)) {
                star.setImageDrawable(deleteFromFavorites(context));
            } else {
                star.setImageDrawable(addToFavorites(context));
            }
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isFavorite = isFavorite(tmdbId, mediaType);
                    callback.onToggleFavorite(tmdbId, mediaType, isFavorite);
                    // Optimistically update UI
                    if (!isFavorite) {
                        star.setImageDrawable(deleteFromFavorites(context));
                    } else {
                        star.setImageDrawable(addToFavorites(context));
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
