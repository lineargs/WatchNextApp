package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.CrewQuery;
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

    public CrewAdapter(@NonNull Context context) {
        this.context = context;
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

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.crew_name)
        TextView crewName;
        @BindView(R.id.crew_job)
        TextView crewJob;
        @BindView(R.id.crew_image_view)
        ImageView profilePath;

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindViews(int position) {
            cursor.moveToPosition(position);
            Log.w("Cursor", DatabaseUtils.dumpCursorToString(cursor));
            crewName.setText(cursor.getString(CrewQuery.CREW_NAME));
            crewJob.setText(cursor.getString(CrewQuery.CREW_JOB));
            Picasso.with(profilePath.getContext())
                    .load(cursor.getString(CrewQuery.PROFILE_PATH))
                    .centerCrop()
                    .error(R.drawable.icon_person_grey)
                    .fit()
                    .into(profilePath);
        }
    }
}

