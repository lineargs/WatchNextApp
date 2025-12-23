package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.CreditsQuery;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.ItemCreditsCastBinding;

/**
 * Created by goranminov on 24/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class CreditsCastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClick callBack;
    private Context context;
    private Cursor cursor;

    public CreditsCastAdapter(@NonNull Context context, @NonNull OnClick listener) {
        this.context = context;
        this.callBack = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCreditsCastBinding binding = ItemCreditsCastBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CreditsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CreditsViewHolder viewHolder = (CreditsViewHolder) holder;
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
        void onPersonClick(String id, String name);
    }

    class CreditsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        final ItemCreditsCastBinding binding;
        final AppCompatTextView name;
        final AppCompatTextView characterName;
        final ImageView photo;

        CreditsViewHolder(ItemCreditsCastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.name = binding.name;
            this.characterName = binding.characterName;
            this.photo = binding.profilePhoto;
            binding.getRoot().setOnClickListener(this);
        }

        void bindViews(int position) {
            cursor.moveToPosition(position);
            name.setText(cursor.getString(CreditsQuery.NAME));
            characterName.setText(cursor.getString(CreditsQuery.CHARACTER_NAME));
            ServiceUtils.loadPicasso(photo.getContext(), cursor.getString(CreditsQuery.PROFILE_PATH))
                    .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                    .centerCrop()
                    .error(R.drawable.icon_person_grey)
                    .into(photo);
        }

        @Override
        public void onClick(View view) {
            cursor.moveToPosition(getAdapterPosition());
            String id = cursor.getString(CreditsQuery.PERSON_ID);
            String name = cursor.getString(CreditsQuery.NAME);
            callBack.onPersonClick(id, name);
        }
    }
}
