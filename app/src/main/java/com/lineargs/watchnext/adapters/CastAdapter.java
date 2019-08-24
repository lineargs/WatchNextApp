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
import com.lineargs.watchnext.data.Credits;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 31/10/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClick callback;
    private Context context;
    private List<Credits> credits;

    public CastAdapter(@NonNull Context context, @NonNull OnClick listener) {
        this.context = context;
        this.callback = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_cast, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CastViewHolder viewHolder = (CastViewHolder) holder;
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

    public void swapCast(List<Credits> credits) {
        this.credits = credits;
        notifyDataSetChanged();
    }

    public interface OnClick {
        void onPersonClick(String id, String name);
    }

    class CastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.cast_name)
        AppCompatTextView castName;
        @BindView(R.id.cast_character_name)
        AppCompatTextView castCharacter;
        @BindView(R.id.cast_image_view)
        ImageView profilePath;

        CastViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (callback != null)
                view.setOnClickListener(this);
        }

        void bindViews(int position) {
            if (credits != null) {
                Credits credit = credits.get(position);
                castName.setText(credit.getName());
                castCharacter.setText(credit.getCharacterName());
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
            callback.onPersonClick(id, name);
        }
    }
}

