package com.lineargs.watchnext.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final Library[] library = {
            new Library("Android support libraries",
                    "The Android Support Library offers backward-compatible versions of a number of features that are not built into the framework",
                    "https://developer.android.com/topic/libraries/support-library/index.html"),
            new Library("ButterKnife",
                    "Field and method binding for Android views",
                    "http://jakewharton.github.io/butterknife/"),
            new Library("Retrofit",
                    "A type-safe HTTP client for Android and Java",
                    "http://square.github.io/retrofit/"),
            new Library("Picasso",
                    "A powerful image downloading and caching library for Android",
                    "http://square.github.io/picasso/"),
            new Library("Firebase Crashlytics",
                    "Lightweight, realtime crash reporter that helps you track, prioritize, and fix stability issues that erode your app quality",
                    "https://firebase.google.com/docs/crashlytics/"),
            new Library("Firebase JobDispatcher",
                    "Library for scheduling background jobs in your Android app",
                    "https://github.com/firebase/firebase-jobdispatcher-android")
    };
    private final int VIEW_TYPE_ABOUT = 0;
    private final int VIEW_TYPE_HEADER = 1;
    private final int VIEW_TYPE_LIBRARY = 2;
    private final Context context;
    OnWebsiteClick onClick;

    public AboutAdapter(Context context, OnWebsiteClick click) {
        this.context = context;
        onClick = click;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ABOUT:
                View about = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_about, parent, false);
                return new AboutHolder(about);
            case VIEW_TYPE_HEADER:
                View header = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_header_library, parent, false);
                return new HeaderHolder(header);
            case VIEW_TYPE_LIBRARY:
                View library = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_library, parent, false);
                return new LibraryHolder(library);
            default:
                throw new IllegalArgumentException("Invalid view type, value of" + viewType);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ABOUT:
                bindAbout((AboutHolder) holder);
                break;
            case VIEW_TYPE_LIBRARY:
                bindLibrary((LibraryHolder) holder, library[position - 2]);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_ABOUT;
        } else if (position == 1) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_LIBRARY;
        }
    }

    @Override
    public int getItemCount() {
        return 1 + 1 + library.length;
    }

    private void bindAbout(final AboutHolder holder) {
        holder.version.setText(Utils.versionString(context));
    }

    private void bindLibrary(final LibraryHolder holder, final Library library) {
        holder.name.setText(library.name);
        holder.description.setText(library.description);
    }

    public interface OnWebsiteClick {
        void onClick(String link);
    }

    private static class Library {
        final String name;
        final String description;
        final String link;

        Library(String name, String description, String link) {
            this.name = name;
            this.description = description;
            this.link = link;
        }
    }

    class LibraryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.library_name)
        TextView name;
        @BindView(R.id.library_description)
        TextView description;
        @BindView(R.id.library_link)
        Button link;

        LibraryHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (onClick != null) link.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;
            onClick.onClick(library[position - 2].link);
        }
    }

    class AboutHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.version_text)
        AppCompatTextView version;

        AboutHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.tmdb_terms)
        public void openTerms() {
            ServiceUtils.openLink(context, context.getString(R.string.tmdb_terms_link));
        }

        @OnClick(R.id.tmdb_api_terms)
        public void openTermsApi() {
            ServiceUtils.openLink(context, context.getString(R.string.tmdb_api_terms_link));
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        HeaderHolder(View view) {
            super(view);
        }
    }
}
