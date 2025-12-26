package com.lineargs.watchnext.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import android.provider.Settings;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.jobs.WorkManagerUtils;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.lineargs.watchnext.databinding.ItemEpisodesBinding;

/**
 * Created by goranminov on 09/12/2017.
 */

public class EpisodesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private java.util.List<com.lineargs.watchnext.data.entity.Episodes> episodes;
    private com.lineargs.watchnext.data.entity.Seasons season;

    public EpisodesAdapter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEpisodesBinding binding = ItemEpisodesBinding.inflate(LayoutInflater.from(context), parent, false);
        // Wait, I noticed I used ItemSearchBinding by mistake in my thought but let me check the original code.
        // Original code used: ItemEpisodesBinding binding = ItemEpisodesBinding.inflate(LayoutInflater.from(context), parent, false);
        return new SeasonsViewHolder(ItemEpisodesBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SeasonsViewHolder viewHolder = (SeasonsViewHolder) holder;
        viewHolder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (episodes == null) {
            return 0;
        } else {
            return episodes.size();
        }
    }

    public void swapEpisodes(java.util.List<com.lineargs.watchnext.data.entity.Episodes> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    public void swapSeason(com.lineargs.watchnext.data.entity.Seasons season) {
        this.season = season;
        notifyDataSetChanged();
    }

    class SeasonsViewHolder extends RecyclerView.ViewHolder {
        
        final ItemEpisodesBinding binding;
        final AppCompatTextView name;
        final ImageView stillPath;
        final AppCompatTextView voteAverage;
        final AppCompatTextView releaseDate;
        final AppCompatTextView overview;
        final AppCompatTextView guestStars;
        final AppCompatTextView directors;
        final AppCompatTextView writers;
        final LinearLayout guestStarsContainer;
        final LinearLayout directorsContainer;
        final LinearLayout writersContainer;
        final FloatingActionButton notification;

        SeasonsViewHolder(ItemEpisodesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.name = binding.name;
            this.stillPath = binding.stillPath;
            this.voteAverage = binding.voteAverage;
            this.releaseDate = binding.releaseDate;
            this.overview = binding.overview;
            this.guestStars = binding.guestStars;
            this.directors = binding.directors;
            this.writers = binding.writers;
            this.guestStarsContainer = binding.guestStarsContainer;
            this.directorsContainer = binding.directorsContainer;
            this.writersContainer = binding.writersContainer;
            this.notification = binding.notificationFab;
            
            this.notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNotification();
                }
            });
        }

        void bindViews(int position) {
            com.lineargs.watchnext.data.entity.Episodes episode = episodes.get(position);
            name.setText(episode.getName());
            voteAverage.setText(episode.getVoteAverage());
            releaseDate.setText(episode.getReleaseDate());
            overview.setText(episode.getOverview());
            if (TextUtils.isEmpty(episode.getGuestStars())) {
                guestStarsContainer.setVisibility(View.GONE);
            } else {
                guestStarsContainer.setVisibility(View.VISIBLE);
                guestStars.setText(episode.getGuestStars());
            }
            if (TextUtils.isEmpty(episode.getDirectors())) {
                directorsContainer.setVisibility(View.GONE);
            } else {
                directorsContainer.setVisibility(View.VISIBLE);
                directors.setText(episode.getDirectors());
            }
            if (TextUtils.isEmpty(episode.getWriters())) {
                writersContainer.setVisibility(View.GONE);
            } else {
                writersContainer.setVisibility(View.VISIBLE);
                writers.setText(episode.getWriters());
            }
            ServiceUtils.loadPicasso(stillPath.getContext(), episode.getStillPath())
                    .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                    .centerInside()
                    .into(stillPath);
        }

        public void setNotification() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.POST_NOTIFICATIONS)) {
                        Snackbar.make(notification, context.getString(R.string.snackbar_notifications_required), Snackbar.LENGTH_LONG)
                                .setAction(context.getString(R.string.snackbar_grant), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                                    }
                                })
                                .show();
                    } else {
                         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                         if (sharedPreferences.getBoolean("PREF_PERMISSION_REQUESTED", false)) {
                             Snackbar.make(notification, context.getString(R.string.snackbar_notifications_disabled), Snackbar.LENGTH_LONG)
                                     .setAction(context.getString(R.string.snackbar_settings), new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent();
                                              intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                              Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                              intent.setData(uri);
                                              context.startActivity(intent);
                                          }
                                     })
                                     .show();
                         } else {
                             sharedPreferences.edit().putBoolean("PREF_PERMISSION_REQUESTED", true).apply();
                             ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                         }
                    }
                    return;
                }
            }
            com.lineargs.watchnext.data.entity.Episodes episode = episodes.get(getAdapterPosition());
            String date = episode.getReleaseDate();
            String name = episode.getName();
            int id = episode.getEpisodeId();
            String title = season != null ? season.getShowName() : "";
            if (!airedAlready(date)) {
                int intervalSeconds = getSeconds(System.currentTimeMillis(), date);
                if (intervalSeconds != 0) {
                    WorkManagerUtils.scheduleReminder(context, intervalSeconds, id,
                            title, name);
                    Toast.makeText(context, context.getString(R.string.toast_notification_reminder), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, context.getString(R.string.toast_aired_already), Toast.LENGTH_SHORT).show();
            }
        }

        private int getSeconds(long today, String date) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getString(R.string.date_pattern), Locale.getDefault());
            Date releaseDay = null;
            try {
                releaseDay = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert releaseDay != null;
            return (int) (TimeUnit.MILLISECONDS.toSeconds(releaseDay.getTime() - today));
        }

        private boolean airedAlready(String date) {
            Date date1 = null;
            if (date != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getString(R.string.date_pattern), Locale.getDefault());
                try {
                    date1 = simpleDateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return date1 != null && (System.currentTimeMillis() > date1.getTime());
        }
    }
}

