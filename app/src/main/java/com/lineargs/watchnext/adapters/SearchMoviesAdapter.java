package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Search;
import com.lineargs.watchnext.data.SearchQuery;
import com.lineargs.watchnext.sync.syncsearch.SearchSyncUtils;
import com.lineargs.watchnext.utils.dbutils.DbUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 06/11/2017.
 * <p>
 * See {@link MainAdapter}
 */

public class SearchMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Search> searches;

    public SearchMoviesAdapter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater
                .from(context)
                .inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchViewHolder searchViewHolder = (SearchViewHolder) holder;
        searchViewHolder.bindViews(context, position);
    }

    @Override
    public int getItemCount() {
        if (searches == null) {
            return 0;
        }
        return searches.size();
    }

    public void swapResults(List<Search> searches) {
        this.searches = searches;
        notifyDataSetChanged();
    }

    public boolean isFavorite(Context context, long id) {
        Uri uri = DataContract.Favorites.buildFavoritesUriWithId(id);
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private VectorDrawableCompat addToFavorites(Context context) {
        return VectorDrawableCompat.create(context.getResources(), R.drawable.icon_add_black, context.getTheme());
    }

    private VectorDrawableCompat deleteFromFavorites(Context context) {
        return VectorDrawableCompat.create(context.getResources(), R.drawable.icon_delete_black, context.getTheme());
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_poster)
        ImageView poster;
        @BindView(R.id.star_image)
        ImageView star;
        @BindView(R.id.main_title)
        TextView title;

        SearchViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindViews(final Context context, int position) {
            if (searches != null) {
                Search search = searches.get(position);
//                if (isFavorite(context, id)) {
//                    star.setImageDrawable(deleteFromFavorites(context));
//                } else {
//                    star.setImageDrawable(addToFavorites(context));
//                }
//                star.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (isFavorite(context, id)) {
//                            DbUtils.removeFromFavorites(context, DataContract.Search.buildSearchUriWithId(id));
//                            Toast.makeText(context, context.getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
//                            star.setImageDrawable(addToFavorites(context));
//                        } else {
//                            SearchSyncUtils.syncSearchMovie(context, String.valueOf(id));
//                            Toast.makeText(context, context.getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
//                            star.setImageDrawable(deleteFromFavorites(context));
//                        }
//                    }
//                });
                title.setText(search.getTitle());
                Picasso.with(poster.getContext())
                        .load(search.getPosterPath())
                        .centerInside()
                        .fit()
                        .into(poster);
            }
        }
    }
}

