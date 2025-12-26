package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.ItemCreditsCrewBinding;

/**
 * Created by goranminov on 21/12/2017.
 */

public class CreditsCrewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClick callBack;
    private Context context;
    private java.util.List<com.lineargs.watchnext.data.entity.Credits> crew;

    public CreditsCrewAdapter(@NonNull Context context, @NonNull OnClick listener) {
        this.context = context;
        this.callBack = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCreditsCrewBinding binding = ItemCreditsCrewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CreditsCrewAdapter.CreditsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CreditsViewHolder viewHolder = (CreditsViewHolder) holder;
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
        void onPersonClick(String id, String name);
    }

    class CreditsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        final ItemCreditsCrewBinding binding;
        final AppCompatTextView name;
        final AppCompatTextView job;
        final ImageView photo;

        CreditsViewHolder(ItemCreditsCrewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.name = binding.crewName;
            this.job = binding.crewJob;
            this.photo = binding.profilePhoto;
            binding.getRoot().setOnClickListener(this);
        }

        void bindViews(int position) {
            com.lineargs.watchnext.data.entity.Credits person = crew.get(position);
            name.setText(person.getName());
            job.setText(person.getJob());
            ServiceUtils.loadPicasso(photo.getContext(), person.getProfilePath())
                    .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                    .centerCrop()
                    .error(R.drawable.icon_person_grey)
                    .into(photo);
        }

        @Override
        public void onClick(View view) {
            com.lineargs.watchnext.data.entity.Credits person = crew.get(getAdapterPosition());
            String id = String.valueOf(person.getPersonId());
            String name = person.getName();
            callBack.onPersonClick(id, name);
        }
    }
}

