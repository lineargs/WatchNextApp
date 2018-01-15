package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.CeditsQuery;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 02/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class CrewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private OnClick onClick;

    public CrewAdapter(@NonNull Context context, @NonNull OnClick onClick) {
        this.context = context;
        this.onClick = onClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_crew, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieViewHolder viewHolder = (MovieViewHolder) holder;
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

    public interface OnClick {
        void onCrewClick(String id);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.crew_name)
        TextView crewName;
        @BindView(R.id.crew_job)
        TextView crewJob;
        @BindView(R.id.crew_image_view)
        ImageView profilePath;

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (onClick != null) {
                view.setOnClickListener(this);
            }
        }

        void bindViews(int position) {
            cursor.moveToPosition(position);
            crewName.setText(cursor.getString(CeditsQuery.NAME));
            crewJob.setText(cursor.getString(CeditsQuery.JOB));
            Picasso.with(profilePath.getContext())
                    .load(cursor.getString(CeditsQuery.PROFILE_PATH))
                    .centerCrop()
                    .error(R.drawable.icon_person_grey)
                    .fit()
                    .into(profilePath);
        }

        @Override
        public void onClick(View view) {
            cursor.moveToPosition(getAdapterPosition());
            String id = cursor.getString(CeditsQuery.PERSON_ID);
            onClick.onCrewClick(id);
        }
    }
}

