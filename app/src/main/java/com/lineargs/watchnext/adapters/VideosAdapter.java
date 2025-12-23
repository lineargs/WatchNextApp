package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.VideosQuery;
import com.squareup.picasso.Picasso;

import com.lineargs.watchnext.databinding.ItemVideosBinding;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClick callback;
    private Context context;
    private Cursor cursor;

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
        if (cursor == null) {
            return 0;
        } else {
            return cursor.getCount();
        }
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
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
            cursor.moveToPosition(position);
            name.setText(cursor.getString(VideosQuery.NAME));
            Picasso.get()
                    .load(cursor.getString(VideosQuery.IMG))
                    .centerInside()
                    .fit()
                    .into(photo);
        }

        @Override
        public void onClick(View view) {
            cursor.moveToPosition(getAdapterPosition());
            callback.OnClick(cursor.getString(VideosQuery.KEY));
        }
    }
}

