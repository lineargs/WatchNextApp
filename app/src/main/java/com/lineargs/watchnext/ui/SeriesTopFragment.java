package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeriesTopAdapter;
import com.lineargs.watchnext.data.DataContract;

/**
 * Created by goranminov on 04/11/2017.
 * <p>
 * A fragment for our Tabbed Series View Pager
 */

public class SeriesTopFragment extends SeriesListFragment implements SeriesTopAdapter.OnItemClickListener {

    private SeriesTopAdapter adapter;

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getContext(), SeriesDetailsActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        adapter = new SeriesTopAdapter(getActivity(), this);
        return adapter;
    }

    @Override
    public void resetLoader(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void swapData(Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public Uri getLoaderUri() {
        return DataContract.TopRatedSerieEntry.CONTENT_URI;
    }
}


