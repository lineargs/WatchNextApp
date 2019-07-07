package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Credits;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 02/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class CrewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Credits> credits;
    private OnClick onClick;

    public CrewAdapter(@NonNull Context context, @NonNull OnClick onClick) {
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_crew, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovieViewHolder viewHolder = (MovieViewHolder) holder;
        viewHolder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (credits == null) {
            return 0;
        } else {
            return credits.size();
        }
    }

    public void swapCrew(List<Credits> credits) {
        this.credits = credits;
        notifyDataSetChanged();
    }

    public interface OnClick {
        void onCrewClick(String id, String name);
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
            if (credits != null) {
                Credits credit = credits.get(position);
                crewName.setText(credit.getName());
                crewJob.setText(credit.getJob());
                Picasso.with(profilePath.getContext())
                        .load(credit.getProfilePath())
                        .centerCrop()
                        .error(R.drawable.icon_person_grey)
                        .fit()
                        .into(profilePath);
            }
        }

        @Override
        public void onClick(View view) {
            Credits credit = credits.get(getAdapterPosition());
            String id = String.valueOf(credit.getPersonId());
            String name = credit.getName();
            onClick.onCrewClick(id, name);
        }
    }
}

