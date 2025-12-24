package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.CreditsQuery;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.squareup.picasso.Picasso;

import com.lineargs.watchnext.databinding.ItemCrewBinding;

/**
 * Created by goranminov on 02/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class CrewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private java.util.List<com.lineargs.watchnext.data.entity.Credits> crew;
    private OnClick onClick;

    public CrewAdapter(@NonNull Context context, @NonNull OnClick onClick) {
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCrewBinding binding = ItemCrewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovieViewHolder viewHolder = (MovieViewHolder) holder;
        viewHolder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (crew == null) {
            return 0;
        } else {
            return crew.size();
        }
    }

    public void swapCrew(java.util.List<com.lineargs.watchnext.data.entity.Credits> crew) {
        this.crew = crew;
        notifyDataSetChanged();
    }

    public interface OnClick {
        void onCrewClick(String id, String name);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        final ItemCrewBinding binding;
        final TextView crewName;
        final TextView crewJob;
        final ImageView profilePath;

        MovieViewHolder(ItemCrewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.crewName = binding.crewName;
            this.crewJob = binding.crewJob;
            this.profilePath = binding.crewImageView;
            if (onClick != null) {
                binding.getRoot().setOnClickListener(this);
            }
        }

        void bindViews(int position) {
            com.lineargs.watchnext.data.entity.Credits credit = crew.get(position);
            crewName.setText(credit.getName());
            crewJob.setText(credit.getJob());
            ServiceUtils.loadPicasso(profilePath.getContext(), credit.getProfilePath())
                    .centerCrop()
                    .error(R.drawable.icon_person_grey)
                    .fit()
                    .into(profilePath);
        }

        @Override
        public void onClick(View view) {
            com.lineargs.watchnext.data.entity.Credits credit = crew.get(getAdapterPosition());
            String id = String.valueOf(credit.getPersonId());
            String name = credit.getName();
            onClick.onCrewClick(id, name);
        }
    }
}

