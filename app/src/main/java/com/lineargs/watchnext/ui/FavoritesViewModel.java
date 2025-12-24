package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.FavoritesRepository;
import com.lineargs.watchnext.data.entity.Favorites;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private final FavoritesRepository repository;
    private final androidx.lifecycle.MediatorLiveData<List<Favorites>> favorites = new androidx.lifecycle.MediatorLiveData<>();
    private final androidx.lifecycle.MutableLiveData<String> sortOrder = new androidx.lifecycle.MutableLiveData<>();
    private static final String ASC = " ASC", DESC = " DESC";
    // We can't access DataContract constants easily without context or static import, but we'll use string literals or pass them.
    // For now, let's allow setting the sort order string.

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoritesRepository(application);
        
        LiveData<List<Favorites>> databaseSource = repository.getFavorites();
        
        favorites.addSource(databaseSource, list -> {
            this.rawFavorites = list;
            updateFavorites();
        });
        
        favorites.addSource(sortOrder, order -> {
            updateFavorites();
        });
    }

    private List<Favorites> rawFavorites;

    private void updateFavorites() {
        if (rawFavorites == null) {
            // Data hasn't loaded yet. Do nothing.
            // When databaseSource emits, this will be called again.
            return;
        }
        
        String order = sortOrder.getValue();
        // If sortOrder is null (not set yet), we can default or just emit raw.
        // But MainActivity sets it immediately.
        
        favorites.setValue(sortList(rawFavorites, order));
    }

    public LiveData<List<Favorites>> getFavorites() {
        return favorites;
    }

    public void setSortOrder(String order) {
        if (java.util.Objects.equals(sortOrder.getValue(), order)) {
            return;
        }
        sortOrder.setValue(order);
    }

    public void removeFavorite(int id) {
        repository.deleteFavorite(id);
    }
    
    private List<Favorites> sortList(List<Favorites> list, String order) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        if (order == null) {
            return list;
        }
        
        java.util.List<Favorites> sorted = new java.util.ArrayList<>(list);
        
        try {
            java.util.Collections.sort(sorted, (o1, o2) -> {
                if (order.equals(com.lineargs.watchnext.data.DataContract.PopularMovieEntry.COLUMN_TITLE + ASC)) {
                    String t1 = o1.getTitle() != null ? o1.getTitle() : "";
                    String t2 = o2.getTitle() != null ? o2.getTitle() : "";
                    return t1.compareToIgnoreCase(t2);
                } else if (order.equals(com.lineargs.watchnext.data.DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + DESC)) {
                     return Double.compare(safeParseDouble(o2.getVoteAverage()), safeParseDouble(o1.getVoteAverage()));
                } else if (order.equals(com.lineargs.watchnext.data.DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + ASC)) {
                     return Double.compare(safeParseDouble(o1.getVoteAverage()), safeParseDouble(o2.getVoteAverage()));
                }
                return 0;
            });
        } catch (IllegalArgumentException e) {
            // Fallback if comparison violates contract, though unlikely with this logic
        }
        
        return sorted;
    }
    
    private double safeParseDouble(String value) {
        if (value == null || value.isEmpty()) return 0.0;
        try {
            // Try standard parse
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            try {
                // Try replacing comma with dot (European locales)
                String sanitized = value.replace(",", ".");
                return Double.parseDouble(sanitized);
            } catch (NumberFormatException e2) {
                // Try extracting the first number found
                try {
                    java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+(\\.\\d+)?)").matcher(value.replace(",", "."));
                    if (m.find()) {
                        return Double.parseDouble(m.group(1));
                    }
                } catch (Exception ex) {
                    // Give up
                }
            }
            return 0.0;
        }
    }
}
