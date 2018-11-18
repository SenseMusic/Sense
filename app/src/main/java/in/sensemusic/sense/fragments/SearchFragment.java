package in.sensemusic.sense.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import in.sensemusic.sense.R;

public class SearchFragment extends Fragment {

    public static ArrayList<String> arrayListSongs;
    public static ArrayAdapter<String> arrayAdapter;
    private HashMap<String, String> albumartData = new HashMap<>();
    private Bundle PlayerSongInfo = new Bundle();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        // Create search cursor
        Cursor searchCursor = Objects.requireNonNull(getActivity()).getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] {
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ALBUM_ID,
                },  //projection
                MediaStore.Audio.Media.IS_MUSIC + " != 0",  //selection
                null,
                MediaStore.Audio.Media.TITLE);  //sortOrder

        // Search List Entries Inflating
        ListView listView = getActivity().findViewById(R.id.listView_Search);
        arrayListSongs = new ArrayList<>();
        while(Objects.requireNonNull(searchCursor).moveToNext()) {
            arrayListSongs.add(searchCursor.getString(searchCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        }

        // Create a ArrayAdapter from List
        arrayAdapter = new ArrayAdapter<>(
                (Context) getActivity(),
                android.R.layout.simple_list_item_1,
                arrayListSongs);

        // Populate ListView with items from ArrayAdapter
        listView.setAdapter(arrayAdapter);

        // Create album art cursor
        Cursor albumArtCursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {
                        MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ALBUM_ART
                },  //projection
                null,
                null,
                null);

        while(Objects.requireNonNull(albumArtCursor).moveToNext())
        {
            albumartData.put(albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums._ID)),
                    albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
        }

        // Set an item click listener for ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {

            // Move search cursor to that position
            if ( !searchCursor.moveToPosition(position) ) {
                throw new IllegalStateException("couldn't move search cursor to position " + position);
            }

            final String title,artist,album,albumart;
            final long _id, time;
            final int seconds, min;

            _id = searchCursor.getLong(searchCursor.getColumnIndex(MediaStore.Audio.Media._ID));
            title =searchCursor.getString(searchCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            artist = searchCursor.getString(searchCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            album = searchCursor.getString(searchCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            time = searchCursor.getLong(searchCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            albumart = albumartData.get(searchCursor.getString(searchCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)))+"";

            /*
            Log.d(TAG,"id:"+_id);
            Log.d(TAG,"title:"+title);
            Log.d(TAG,"artist:"+artist);
            Log.d(TAG,"album:"+album);
            Log.d(TAG,"time:"+time+"ms");
            Log.d(TAG,"albumart:"+albumart);
            */

            seconds = (int) (time/1000);
            min = seconds/60;

            PlayerSongInfo.putLong(MediaStore.Audio.Media._ID, _id);
            PlayerSongInfo.putString(MediaStore.Audio.Media.TITLE, title);
            PlayerSongInfo.putString(MediaStore.Audio.Media.ARTIST, artist);
            PlayerSongInfo.putString(MediaStore.Audio.Media.ALBUM, album);
            PlayerSongInfo.putString(MediaStore.Audio.Media.DURATION, +min+":"+(seconds%60));
            PlayerSongInfo.putString(MediaStore.Audio.Albums.ALBUM_ART, albumart);

            PlayerFragment playerFragment = new PlayerFragment();
            playerFragment.setArguments(PlayerSongInfo);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_main,playerFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
