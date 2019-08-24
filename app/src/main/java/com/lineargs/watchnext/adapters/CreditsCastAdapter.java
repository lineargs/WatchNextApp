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
import com.lineargs.watchnext.utils.ServiceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 24/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class CreditsCastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClick callBack;
    private Context context;
    private List<Credits> credits;

    public CreditsCastAdapter(@NonNull Context context, @NonNull OnClick listener) {
        this.context = context;
        this.callBack = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_credits_cast, parent, false);
        return new CreditsViewHolder(view);
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

    public void swapCast(List<Credits> credits) {
        this.credits = credits;
        notifyDataSetChanged();
    }

    public interface OnClick {
        void onPersonClick(String id, String name);
    }

    class CreditsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.name)
        AppCompatTextView name;
        @BindView(R.id.character_name)
        AppCompatTextView characterName;
        @BindView(R.id.profile_photo)
        ImageView photo;

        CreditsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        void bindViews(int position) {
            if (credits != null) {
                Credits credit = credits.get(position);
                name.setText(credit.getName());
                characterName.setText(credit.getCharacterName());
                ServiceUtils.loadPicasso(photo.getContext(), credit.getProfilePath())
                        .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                        .centerCrop()
                        .error(R.drawable.icon_person_grey)
                        .into(photo);
            }
        }

        @Override
        public void onClick(View view) {
            if (credits != null) {
                Credits credit = credits.get(getAdapterPosition());
                String id = String.valueOf(credit.getPersonId());
                String name = credit.getName();
                callBack.onPersonClick(id, name);
            }
        }
    }
}
