package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.databinding.ItemReviewBinding;

/**
 * Created by Goran Minov on 22/11/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClickListener callback;
    private Context context;
    private java.util.List<com.lineargs.watchnext.data.entity.Review> reviews;

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
        if (reviews == null) {
            return 0;
        } else {
            return reviews.size();
        }
    }

    public void swapReviews(java.util.List<com.lineargs.watchnext.data.entity.Review> reviews) {
        this.reviews = reviews;
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
            com.lineargs.watchnext.data.entity.Review review = reviews.get(position);
            author.setText(review.getAuthor());
            content.setText(review.getContent());
        }

        @Override
        public void onClick(View view) {
            com.lineargs.watchnext.data.entity.Review review = reviews.get(getAdapterPosition());
            String url = review.getUrl();
            callback.OnClick(url);
        }
    }
}
