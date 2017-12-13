package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.ReviewQuery;

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
    private Cursor cursor;

    public ReviewAdapter(@NonNull Context context, OnClickListener listener) {
        this.context = context;
        callback = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
