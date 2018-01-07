package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.ExplorePopularAdapter;
import com.lineargs.watchnext.data.DataContract;

/**
 * Created by goranminov on 03/11/2017.
 * <p>
 * A fragment for our Tabbed Movies View Pager
 */

public class NavPopularFragment extends MovieListFragment implements ExplorePopularAdapter.OnItemClickListener {


    private ExplorePopularAdapter mAdapter;

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAdapter = new ExplorePopularAdapter(getActivity(), this);
        return mAdapter;
    }

    @Override
    public Uri getLoaderUri() {
        return DataContract.PopularMovieEntry.CONTENT_URI;
    }

    @Override
    public void swapData(Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void resetLoader(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

}
