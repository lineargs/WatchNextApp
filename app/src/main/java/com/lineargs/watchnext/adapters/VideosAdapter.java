package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Videos;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClick callback;
    private Context context;
    private List<Videos> videos;

    public VideosAdapter(@NonNull Context context, OnItemClick listener) {
        this.context = context;
        callback = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_videos, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoViewHolder viewHolder = (VideoViewHolder) holder;
        viewHolder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (videos == null) {
            return 0;
        } else {
            return videos.size();
        }
    }

    public void swapVideos(List<Videos> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public interface OnItemClick {
        void OnClick(String key);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.name)
        AppCompatTextView name;
        @BindView(R.id.you_tube_image)
        ImageView photo;

        VideoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (callback != null) view.setOnClickListener(this);
        }

        void bindViews(int position) {
            if (videos != null) {
                Videos video = videos.get(position);
                name.setText(video.getName());
                Picasso.with(photo.getContext())
                        .load(video.getImage())
                        .centerInside()
                        .fit()
                        .into(photo);
            }
        }

        @Override
        public void onClick(View view) {
            Videos video = videos.get(getAdapterPosition());
            callback.OnClick(video.getKey());
        }
    }
}

