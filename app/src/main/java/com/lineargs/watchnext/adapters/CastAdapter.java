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
import com.squareup.picasso.Picasso;

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
    private Cursor cursor;

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
            cursor.moveToPosition(position);
            castName.setText(cursor.getString(CreditsQuery.NAME));
            castCharacter.setText(cursor.getString(CreditsQuery.CHARACTER_NAME));
            Picasso.get()
                    .load(cursor.getString(CreditsQuery.PROFILE_PATH))
                    .centerCrop()
                    .error(R.drawable.icon_person_grey)
                    .fit()
                    .into(profilePath);
        }

        @Override
        public void onClick(View view) {
            cursor.moveToPosition(getAdapterPosition());
            String id = cursor.getString(CreditsQuery.PERSON_ID);
            String name = cursor.getString(CreditsQuery.NAME);
            callback.onPersonClick(id, name);
        }
    }
}

