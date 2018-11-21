package in.sensemusic.sense.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import in.sensemusic.sense.R;
import in.sensemusic.sense.activities.MainActivity;
import in.sensemusic.sense.adapters.AlbumAdapter;

public class AlbumFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.recyclerView_Album);
        LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //Log.d("Sense","Album Called");

        String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART,
        };

        ContentResolver content = getActivity().getContentResolver();
        Cursor media_cursor = content.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,//Selection Statement
                null,//Selection Arguments replacement for ? in where id=?
                MediaStore.Audio.Albums.ALBUM + "");

        recyclerView.setAdapter(new AlbumAdapter(getContext(), media_cursor));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Action Bar title
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle("Albums");
        // ((MainActivity) getActivity()).getSupportActionBar().setTitle("Albums");
    }

}