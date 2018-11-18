package in.sensemusic.sense.loaders;

import android.content.Context;

import in.sensemusic.sense.models.Artist;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArtistsViewModel extends ViewModel {
    private MutableLiveData<List<Artist>> artists;

    public LiveData<List<Artist>> getArtists(@NonNull final Context context) {
        if (artists == null) {
            artists = new MutableLiveData<>();
            loadUsers(context);
        }
        return artists;
    }

    private void loadUsers(@NonNull Context context) {
        artists.setValue(ArtistProvider.getAllArtists(context));
    }
}