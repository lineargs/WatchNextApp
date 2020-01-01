package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.episodes.Episodes;
import com.lineargs.watchnext.jobs.ReminderFirebaseUtilities;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by goranminov on 09/12/2017.
 * <p>
 * Same ol' episodes adapter
 */

public class EpisodesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Episodes> episodesList;

    public EpisodesAdapter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_episodes, parent, false);
        return new SeasonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SeasonsViewHolder viewHolder = (SeasonsViewHolder) holder;
        viewHolder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (episodesList == null) {
            return 0;
        } else {
            return episodesList.size();
        }
    }

    public void swapData(List<Episodes> episodesList) {
        this.episodesList = episodesList;
        notifyDataSetChanged();
    }

    class SeasonsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        AppCompatTextView name;
        @BindView(R.id.still_path)
        ImageView stillPath;
        @BindView(R.id.vote_average)
        AppCompatTextView voteAverage;
        @BindView(R.id.release_date)
        AppCompatTextView releaseDate;
        @BindView(R.id.overview)
        AppCompatTextView overview;
        @BindView(R.id.guest_stars)
        AppCompatTextView guestStars;
        @BindView(R.id.directors)
        AppCompatTextView directors;
        @BindView(R.id.writers)
        AppCompatTextView writers;
        @BindView(R.id.guest_stars_container)
        LinearLayout guestStarsContainer;
        @BindView(R.id.directors_container)
        LinearLayout directorsContainer;
        @BindView(R.id.writers_container)
        LinearLayout writersContainer;

        SeasonsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindViews(int position) {
            Episodes episodes = episodesList.get(position);
            name.setText(episodes.getName());
            voteAverage.setText(episodes.getVoteAverage());
            releaseDate.setText(episodes.getReleaseDate());
            overview.setText(episodes.getOverview());
            if (TextUtils.isEmpty(episodes.getGuestStars())) {
                guestStarsContainer.setVisibility(View.GONE);
            } else {
                guestStars.setText(episodes.getGuestStars());
            }
            if (TextUtils.isEmpty(episodes.getDirectors())) {
                directorsContainer.setVisibility(View.GONE);
            } else {
                directors.setText(episodes.getDirectors());
            }
            if (TextUtils.isEmpty(episodes.getWriters())) {
                writersContainer.setVisibility(View.GONE);
            } else {
                writers.setText(episodes.getWriters());
            }
            ServiceUtils.loadPicasso(stillPath.getContext(), episodes.getStillPath())
                    .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                    .centerInside()
                    .into(stillPath);
        }

        @OnClick(R.id.notification_fab)
        void setNotification() {
            Episodes episodes = episodesList.get(getAdapterPosition());
            String date = episodes.getReleaseDate();
            String name = episodes.getName();
            int id = episodes.getId();
            String title = "";
//                    backCursor.getString(SeasonsQuery.SHOW_NAME);
            if (!airedAlready(date)) {
                int intervalSeconds = getSeconds(System.currentTimeMillis(), date);
                ReminderFirebaseUtilities.scheduleReminder(context, intervalSeconds, id,
                        title, name);
                Toast.makeText(context, context.getString(R.string.toast_notification_reminder), Toast.LENGTH_SHORT).show();
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

