package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Credits;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 21/12/2017.
 */

public class CreditsCrewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClick callBack;
    private Context context;
    private List<Credits> credits;

    public CreditsCrewAdapter(@NonNull Context context, @NonNull OnClick listener) {
        this.context = context;
        this.callBack = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_credits_crew, parent, false);
        return new CreditsCrewAdapter.CreditsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CreditsViewHolder viewHolder = (CreditsViewHolder) holder;
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
        void onPersonClick(String id, String name);
    }

    class CreditsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.crew_name)
        AppCompatTextView name;
        @BindView(R.id.crew_job)
        AppCompatTextView job;
        @BindView(R.id.profile_photo)
        ImageView photo;

        CreditsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        void bindViews(int position) {
            if (credits != null) {
                Credits crew = credits.get(position);
                name.setText(crew.getName());
                job.setText(crew.getJob());
                ServiceUtils.loadPicasso(photo.getContext(), crew.getProfilePath())
                        .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                        .centerCrop()
                        .error(R.drawable.icon_person_grey)
                        .into(photo);
            }
        }

        @Override
        public void onClick(View view) {
//            cursor.moveToPosition(getAdapterPosition());
//            String id = cursor.getString(CreditsQuery.PERSON_ID);
//            String name = cursor.getString(CreditsQuery.NAME);
//            callBack.onPersonClick(id, name);
        }
    }
}

