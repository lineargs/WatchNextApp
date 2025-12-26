package com.lineargs.watchnext.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.ItemCastBinding;

/**
 * Created by goranminov on 31/10/2017.
 */

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClick callback;
    private Context context;
    private java.util.List<com.lineargs.watchnext.data.entity.Credits> cast;

    public CastAdapter(@NonNull Context context, @NonNull OnClick listener) {
        this.context = context;
        this.callback = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCastBinding binding = ItemCastBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CastViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CastViewHolder viewHolder = (CastViewHolder) holder;
        viewHolder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (cast == null) {
            return 0;
        } else {
            return cast.size();
        }
    }

    public void swapCast(java.util.List<com.lineargs.watchnext.data.entity.Credits> cast) {
        this.cast = cast;
        notifyDataSetChanged();
    }

    public interface OnClick {
        void onPersonClick(String id, String name);
    }

    class CastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        final ItemCastBinding binding;
        final AppCompatTextView castName;
        final AppCompatTextView castCharacter;
        final ImageView profilePath;

        CastViewHolder(ItemCastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.castName = binding.castName;
            this.castCharacter = binding.castCharacterName;
            this.profilePath = binding.castImageView;
            if (callback != null)
                binding.getRoot().setOnClickListener(this);
        }

        void bindViews(int position) {
            com.lineargs.watchnext.data.entity.Credits credit = cast.get(position);
            castName.setText(credit.getName());
            castCharacter.setText(credit.getCharacterName());
            ServiceUtils.loadPicasso(profilePath.getContext(), credit.getProfilePath())
                    .centerCrop()
                    .error(R.drawable.icon_person_grey)
                    .fit()
                    .into(profilePath);
        }

        @Override
        public void onClick(View view) {
            com.lineargs.watchnext.data.entity.Credits credit = cast.get(getAdapterPosition());
            String id = String.valueOf(credit.getPersonId());
            String name = credit.getName();
            callback.onPersonClick(id, name);
        }
    }
}

