package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.entity.UpcomingEpisodes;
import com.lineargs.watchnext.databinding.ItemScheduleBinding;
import com.lineargs.watchnext.tools.SeasonTools;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduleAdapter extends ListAdapter<UpcomingEpisodes, ScheduleAdapter.ScheduleViewHolder> {

    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(UpcomingEpisodes episode);
    }

    public ScheduleAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<UpcomingEpisodes> DIFF_CALLBACK = new DiffUtil.ItemCallback<UpcomingEpisodes>() {
        @Override
        public boolean areItemsTheSame(@NonNull UpcomingEpisodes oldItem, @NonNull UpcomingEpisodes newItem) {
            return oldItem.getEpisodeId() == newItem.getEpisodeId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull UpcomingEpisodes oldItem, @NonNull UpcomingEpisodes newItem) {
            return oldItem.getAirDate().equals(newItem.getAirDate()) &&
                    oldItem.getEpisodeName().equals(newItem.getEpisodeName()) &&
                    (oldItem.getPosterPath() != null ? oldItem.getPosterPath().equals(newItem.getPosterPath()) : newItem.getPosterPath() == null);
        }
    };

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemScheduleBinding binding = ItemScheduleBinding.inflate(inflater, parent, false);
        return new ScheduleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private final ItemScheduleBinding binding;
        private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/";

        ScheduleViewHolder(ItemScheduleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(UpcomingEpisodes episode, OnItemClickListener listener) {
            Context context = binding.getRoot().getContext();
            binding.seriesTitle.setText(episode.getSeriesTitle());
            
            String episodeFormat = SeasonTools.getEpisodeFormat(context, episode.getSeasonNumber(), episode.getEpisodeNumber());
            binding.episodeName.setText(String.format("%s: %s", episodeFormat, episode.getEpisodeName()));
            
            binding.airDate.setText(formatDate(episode.getAirDate()));
            
            String posterPath = episode.getPosterPath();
            if (!TextUtils.isEmpty(posterPath)) {
                if (!posterPath.startsWith("http")) {
                    if (posterPath.startsWith("/")) {
                        posterPath = posterPath.substring(1);
                    }
                    posterPath = IMAGE_BASE_URL + posterPath;
                }
                
                ServiceUtils.loadPicasso(context, posterPath)
                        .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_serie)
                        .error(R.drawable.placeholder_serie)
                        .into(binding.seriesPoster);
            } else {
                binding.seriesPoster.setImageResource(R.drawable.placeholder_serie);
            }

            binding.getRoot().setOnClickListener(v -> listener.onItemClick(episode));
        }

        private String formatDate(String dateString) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
            try {
                Date date = inputFormat.parse(dateString);
                if (date != null) {
                    return outputFormat.format(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateString;
        }
    }
}
