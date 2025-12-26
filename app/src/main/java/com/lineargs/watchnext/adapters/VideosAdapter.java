package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import com.lineargs.watchnext.databinding.ItemVideosBinding;

/**
 * Created by goranminov on 26/11/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClick callback;
    private Context context;
    private java.util.List<com.lineargs.watchnext.data.entity.Videos> videos;

    public VideosAdapter(@NonNull Context context, OnItemClick listener) {
        this.context = context;
        callback = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideosBinding binding = ItemVideosBinding.inflate(LayoutInflater.from(context), parent, false);
        return new VideoViewHolder(binding);
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

    public void swapVideos(java.util.List<com.lineargs.watchnext.data.entity.Videos> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public interface OnItemClick {
        void OnClick(String key);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        final ItemVideosBinding binding;
        final AppCompatTextView name;
        final ImageView photo;

        VideoViewHolder(ItemVideosBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.name = binding.name;
            this.photo = binding.youTubeImage;
            if (callback != null) binding.getRoot().setOnClickListener(this);
        }

        void bindViews(int position) {
            com.lineargs.watchnext.data.entity.Videos video = videos.get(position);
            name.setText(video.getName());
            Picasso.get()
                    .load(video.getImage())
                    .centerInside()
                    .fit()
                    .into(photo);
        }

        @Override
        public void onClick(View view) {
            com.lineargs.watchnext.data.entity.Videos video = videos.get(getAdapterPosition());
            callback.OnClick(video.getKey());
        }
    }
}

