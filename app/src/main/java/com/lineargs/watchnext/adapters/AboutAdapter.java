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
            new Library(R.string.lib_android_jetpack_name,
                    R.string.lib_android_jetpack_desc,
                    "https://developer.android.com/jetpack/"),
            new Library(R.string.lib_retrofit_name,
                    R.string.lib_retrofit_desc,
                    "http://square.github.io/retrofit/"),
            new Library(R.string.lib_picasso_name,
                    R.string.lib_picasso_desc,
                    "http://square.github.io/picasso/"),
            new Library(R.string.lib_crashlytics_name,
                    R.string.lib_crashlytics_desc,
                    "https://firebase.google.com/docs/crashlytics/"),
            new Library(R.string.lib_fcm_name,
                    R.string.lib_fcm_desc,
                    "https://firebase.google.com/docs/cloud-messaging/"),
            new Library(R.string.lib_workmanager_name,
                    R.string.lib_workmanager_desc,
                    "https://developer.android.com/topic/libraries/architecture/workmanager"),
            new Library(R.string.lib_room_name,
                    R.string.lib_room_desc,
                    "https://developer.android.com/topic/libraries/architecture/room")
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
        holder.name.setText(library.nameRes);
        holder.description.setText(library.descriptionRes);
    }

    public interface OnWebsiteClick {
        void onClick(String link);
    }

    private static class Library {
        final int nameRes;
        final int descriptionRes;
        final String link;

        Library(int nameRes, int descriptionRes, String link) {
            this.nameRes = nameRes;
            this.descriptionRes = descriptionRes;
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
