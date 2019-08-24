package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Reviews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 22/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClickListener callback;
    private Context context;
    private List<Reviews> reviews;

    public ReviewAdapter(@NonNull Context context, OnClickListener listener) {
        this.context = context;
        callback = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
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

    public void swapReviews(List<Reviews> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void OnClick(String url);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.review_author)
        AppCompatTextView author;
        @BindView(R.id.review_content)
        AppCompatTextView content;

        ReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (callback != null) view.setOnClickListener(this);
        }

        void bindViews(int position) {
            if (reviews != null) {
                Reviews review = reviews.get(position);
                author.setText(review.getAuthor());
                content.setText(review.getContent());
            }
        }

        @Override
        public void onClick(View view) {
            Reviews review = reviews.get(getAdapterPosition());
            String url = review.getUrl();
            callback.OnClick(url);
        }
    }
}
