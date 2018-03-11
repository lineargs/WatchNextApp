package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.AboutAdapter;
import com.lineargs.watchnext.utils.ServiceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutFragment extends Fragment implements AboutAdapter.OnWebsiteClick {

    @BindView(R.id.library_recycler_view)
    RecyclerView libraryRecyclerView;
    private Unbinder unbinder;

    public AboutFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        libraryRecyclerView.setAdapter(new AboutAdapter(getActivity(), this));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(String link) {
        ServiceUtils.openLink(getActivity(), link);
    }
}
