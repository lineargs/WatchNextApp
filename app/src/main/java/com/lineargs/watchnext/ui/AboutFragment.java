package com.lineargs.watchnext.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.AboutAdapter;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.FragmentAboutBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutFragment extends Fragment implements AboutAdapter.OnWebsiteClick {

    private FragmentAboutBinding binding;

    public AboutFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        binding.libraryRecyclerView.setAdapter(new AboutAdapter(getActivity(), this));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(String link) {
        ServiceUtils.openWeb(getActivity(), link);
    }
}
