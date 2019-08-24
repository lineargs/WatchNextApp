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
import com.lineargs.watchnext.data.Seasons;
import com.lineargs.watchnext.tools.SeasonTools;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 27/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class SeasonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClickListener callback;
    private Context context;
    private List<Seasons> seasons;

    public SeasonsAdapter(@NonNull Context context, OnClickListener listener) {
        this.context = context;
        this.callback = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_seasons, parent, false);
        return new SeasonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SeasonsViewHolder viewHolder = (SeasonsViewHolder) holder;
        viewHolder.bindViews(context, position);
    }

    @Override
    public int getItemCount() {
        if (seasons == null) {
            return 0;
        } else {
            return seasons.size();
        }
    }

    public void swapSeasons(List<Seasons> seasons) {
        this.seasons = seasons;
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void OnClick(String seasonId, int seasonNumber, String episodes);
    }

    class SeasonsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title)
        AppCompatTextView title;
        @BindView(R.id.seasons_episodes)
        AppCompatTextView episodes;
        @BindView(R.id.poster_path)
        ImageView poster;

        SeasonsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (callback != null) view.setOnClickListener(this);
        }

        void bindViews(Context context, int position) {
            Resources resources = context.getResources();
            if (seasons != null) {
                Seasons season = seasons.get(position);
                title.setText(SeasonTools.getSeasonString(context, Integer.parseInt(season.getSeasonNumber())));
                String episodesCount = resources.getQuantityString(R.plurals.numberOfEpisodes, season.getEpisodeCount(), season.getEpisodeCount());
                episodes.setText(episodesCount);
                Picasso.with(poster.getContext())
                        .load(season.getPosterPath())
                        .centerCrop()
                        .fit()
                        .into(poster);
            }
        }

        @Override
        public void onClick(View view) {
            Resources resources = context.getResources();
            Seasons season = seasons.get(getAdapterPosition());
            String seasonId = String.valueOf(season.getSeasonId());
            String episodes = resources.getQuantityString(R.plurals.numberOfEpisodes, season.getEpisodeCount(), season.getEpisodeCount());
            int number = Integer.parseInt(season.getSeasonNumber());
            callback.OnClick(seasonId, number, episodes);
        }
    }
}

