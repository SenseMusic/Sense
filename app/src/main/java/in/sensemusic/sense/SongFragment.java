package in.sensemusic.sense;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;

/*
Recycler View
1. Initialize RecylerView & Layout Manager
2. SetLayoutManager On RecylerView
*/

public class SongFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView_Songs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID
              };

        ContentResolver content = getActivity().getContentResolver();
        Cursor song_cursor = content.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);

        recyclerView.setAdapter(new SongAdapter(getContext(),song_cursor));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Action Bar title
        ((MainActivity) getActivity()).setActionBarTitle("Songs");
        // ((MainActivity) getActivity()).getSupportActionBar().setTitle("Albums");
    }

}

class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{
    private Cursor songCursor;
    private Context context;
    private LayoutInflater layoutInflater;
    private static String TAG = "sense";
    private HashMap<String, String> albumartData;
    private Bundle PlayerSongInfo  = new Bundle();

    public SongAdapter(Context context, Cursor songCursor) {
        this.songCursor = songCursor;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);

        albumartData = new HashMap<String, String>();

        String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        ContentResolver content = context.getContentResolver();
        Cursor albumArtCursor = content.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null, //new String[]{songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))},
                null);

        while(albumArtCursor.moveToNext())
        {
            albumartData.put(albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums._ID)),
                    albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
         //   Log.d(TAG,albumartData.get(AlbumArtCursor.getString(AlbumArtCursor.getColumnIndex(MediaStore.Audio.Albums._ID)))+"Albumdata");
        }
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(TAG,"called OnCreateViewHolder()");
        View view = layoutInflater.inflate(R.layout.song_name,parent,false);
        SongViewHolder songViewHolder = new SongViewHolder(view);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {

        if ( !songCursor.moveToPosition(position) ) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        final String title,artist,album,albumart;
        final long _id, time;
        final int seconds, min;

        _id = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media._ID));
        title =songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        artist = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        album = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        time = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        albumart = albumartData.get(songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)))+"";

        holder.txt_song_name.setText(title);
        holder.txt_artist.setText(artist);
        holder.txt_album.setText(album);

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
        holder.txt_duration.setText(min+":"+(seconds%60));

        holder.txt_song_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerSongInfo.putLong(MediaStore.Audio.Media._ID, _id);
                PlayerSongInfo.putString(MediaStore.Audio.Media.TITLE, title);
                PlayerSongInfo.putString(MediaStore.Audio.Media.ARTIST, artist);
                PlayerSongInfo.putString(MediaStore.Audio.Media.ALBUM, album);
                PlayerSongInfo.putString(MediaStore.Audio.Media.DURATION, +min+":"+(seconds%60));
                PlayerSongInfo.putString(MediaStore.Audio.Albums.ALBUM_ART, albumart);

                PlayerFragment playerFragment = new PlayerFragment();
                playerFragment.setArguments(PlayerSongInfo);
                FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main,playerFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        Glide
                .with(context)
                .load(albumart)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.album_art)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                )
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions()
                        .crossFade()
                )
                .into(holder.img_AlbumArt);

    }

    @Override
    public int getItemCount() {
        return songCursor.getCount();
    }

    class SongViewHolder extends RecyclerView.ViewHolder{

        TextView txt_song_name,txt_duration,txt_artist,txt_album;
        ImageView img_AlbumArt;

        public SongViewHolder(View itemView) {
            super(itemView);
            txt_song_name = itemView.findViewById(R.id.SongName);
            txt_duration = itemView.findViewById(R.id.time);
            txt_album = itemView.findViewById(R.id.Album);
            txt_artist = itemView.findViewById(R.id.Artist);
            img_AlbumArt = itemView.findViewById(R.id.imageView_AlbumArt);
        }
    }
}