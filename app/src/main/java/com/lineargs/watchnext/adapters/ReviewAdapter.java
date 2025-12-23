package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.ReviewQuery;

import com.lineargs.watchnext.databinding.ItemReviewBinding;

/**
 * Created by goranminov on 22/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClickListener callback;
    private Context context;
    private Cursor cursor;

    public ReviewAdapter(@NonNull Context context, OnClickListener listener) {
        this.context = context;
        callback = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewBinding binding = ItemReviewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReviewViewHolder viewHolder = (ReviewViewHolder) holder;
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

    public interface OnClickListener {
        void OnClick(String url);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        final ItemReviewBinding binding;
        final AppCompatTextView author;
        final AppCompatTextView content;

        ReviewViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.author = binding.reviewAuthor;
            this.content = binding.reviewContent;
            if (callback != null) binding.getRoot().setOnClickListener(this);
        }

        void bindViews(int position) {
            cursor.moveToPosition(position);
            author.setText(cursor.getString(ReviewQuery.AUTHOR));
            content.setText(cursor.getString(ReviewQuery.CONTENT));
        }

        @Override
        public void onClick(View view) {
            cursor.moveToPosition(getAdapterPosition());
            String url = cursor.getString(ReviewQuery.URL);
            callback.OnClick(url);
        }
    }
}
