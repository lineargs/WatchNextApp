package com.lineargs.watchnext.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.Utils;

import com.lineargs.watchnext.databinding.ItemAboutBinding;
import com.lineargs.watchnext.databinding.ItemHeaderLibraryBinding;
import com.lineargs.watchnext.databinding.ItemLibraryBinding;

public class AboutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final Library[] library = {
            //TODO Move them to strings resources for translating
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
                    "https://github.com/firebase/firebase-jobdispatcher-android"),
            new Library("Firebase Cloud Messaging",
                    "Firebase Cloud Messaging (FCM) is a cross-platform messaging solution that lets you reliably deliver messages at no cost.",
                    "https://firebase.google.com/docs/cloud-messaging/")
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ABOUT:
                ItemAboutBinding aboutBinding = ItemAboutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new AboutHolder(aboutBinding);
            case VIEW_TYPE_HEADER:
                ItemHeaderLibraryBinding headerBinding = ItemHeaderLibraryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new HeaderHolder(headerBinding);
            case VIEW_TYPE_LIBRARY:
                ItemLibraryBinding libraryBinding = ItemLibraryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new LibraryHolder(libraryBinding);
            default:
                throw new IllegalArgumentException("Invalid view type, value of" + viewType);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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

        final ItemLibraryBinding binding;
        final TextView name;
        final TextView description;
        final Button link;

        LibraryHolder(ItemLibraryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.name = binding.libraryName;
            this.description = binding.libraryDescription;
            this.link = binding.libraryLink;
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

        final ItemAboutBinding binding;
        final AppCompatTextView version;

        AboutHolder(ItemAboutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.version = binding.versionText;
            
            binding.tmdbTerms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTerms();
                }
            });
            binding.tmdbApiTerms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTermsApi();
                }
            });
        }

        public void openTerms() {
            ServiceUtils.openWeb(context, context.getString(R.string.tmdb_terms_link));
        }

        public void openTermsApi() {
            ServiceUtils.openWeb(context, context.getString(R.string.tmdb_api_terms_link));
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        HeaderHolder(ItemHeaderLibraryBinding binding) {
            super(binding.getRoot());
        }
    }
}
