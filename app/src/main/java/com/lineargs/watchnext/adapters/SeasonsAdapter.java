package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.tools.SeasonTools;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.ItemSeasonsBinding;

/**
 * Created by goranminov on 27/11/2017.
 */

public class SeasonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClickListener callback;
    private Context context;
    private java.util.List<com.lineargs.watchnext.data.entity.Seasons> seasons;

    public SeasonsAdapter(@NonNull Context context, OnClickListener listener) {
        this.context = context;
        this.callback = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSeasonsBinding binding = ItemSeasonsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new SeasonsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SeasonsViewHolder viewHolder = (SeasonsViewHolder) holder;
        viewHolder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (seasons == null) {
            return 0;
        } else {
            return seasons.size();
        }
    }

    public void swapSeasons(java.util.List<com.lineargs.watchnext.data.entity.Seasons> seasons) {
        this.seasons = seasons;
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void OnClick(String seasonId, int seasonNumber, String serieId, String episodes);
    }

    class SeasonsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        final ItemSeasonsBinding binding;
        final AppCompatTextView title;
        final AppCompatTextView episodes;
        final ImageView poster;

        SeasonsViewHolder(ItemSeasonsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.title = binding.title;
            this.episodes = binding.seasonsEpisodes;
            this.poster = binding.posterPath;
            if (callback != null) binding.getRoot().setOnClickListener(this);
        }

        void bindViews(int position) {
            Resources resources = context.getResources();
            com.lineargs.watchnext.data.entity.Seasons season = seasons.get(position);
            int number = 0;
            try {
                 number = Integer.parseInt(season.getSeasonNumber());
            } catch (NumberFormatException e) {}

            title.setText(SeasonTools.getSeasonString(context, number));
            String episodesCount = resources.getQuantityString(R.plurals.numberOfEpisodes, season.getEpisodeCount(), season.getEpisodeCount());
            episodes.setText(episodesCount);
            ServiceUtils.loadPicasso(context, season.getPosterPath())
                    .centerCrop()
                    .fit()
                    .into(poster);
        }

        @Override
        public void onClick(View view) {
            Resources resources = context.getResources();
            com.lineargs.watchnext.data.entity.Seasons season = seasons.get(getAdapterPosition());
            String seasonId = String.valueOf(season.getSeasonId());
            String episodesCount = resources.getQuantityString(R.plurals.numberOfEpisodes, season.getEpisodeCount(), season.getEpisodeCount());
            String serieId = String.valueOf(season.getTmdbId());
            int number = 0;
            try {
                 number = Integer.parseInt(season.getSeasonNumber());
            } catch (NumberFormatException e) {}
            callback.OnClick(seasonId, number, serieId, episodesCount);
        }
    }
}

