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

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Cursor song_cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView_Songs);
        layoutManager = new LinearLayoutManager(getContext());
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
        song_cursor = content.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                new String(MediaStore.Audio.Media.TITLE));

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
    private Cursor AlbumArtCursor;
    private HashMap<String, String> albumartData;
    private Bundle PlayserSongInfo  = new Bundle();

    public SongAdapter(Context context, Cursor songCursor) {
        this.songCursor = songCursor;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        PlayserSongInfo  = new Bundle();

        albumartData = new HashMap<String, String>();

        String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        ContentResolver content = context.getContentResolver();
        AlbumArtCursor = content.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null, //new String[]{songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))},
                null);

        while(AlbumArtCursor.moveToNext())
        {
            albumartData.put(AlbumArtCursor.getString(AlbumArtCursor.getColumnIndex(MediaStore.Audio.Albums._ID)),
                    AlbumArtCursor.getString(AlbumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
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
        if (!songCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        final String title,artist,album,albumart;
        final long id;
        id = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media._ID));
        title =songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        artist = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        album = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        albumart = albumartData.get(songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)))+"";
        holder.txt_song_name.setText(title);
        holder.txt_artist.setText(artist);
        holder.txt_album.setText(album);

        Long time = songCursor.getLong(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        //Log.d(TAG,time+"ms");
        final int seconds = (int) (time/1000);
        final int min = seconds/60;

        holder.txt_duration.setText(min+":"+(seconds%60));

        holder.txt_song_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayserSongInfo.putString(MediaStore.Audio.Media.TITLE,
                        title);
                PlayserSongInfo.putString(MediaStore.Audio.Media.ALBUM,
                        album);
                PlayserSongInfo.putString(MediaStore.Audio.Media.ARTIST,
                        artist);
                PlayserSongInfo.putString(MediaStore.Audio.Media.DURATION,
                       +min+":"+(seconds%60));

                PlayserSongInfo.putString(MediaStore.Audio.Albums.ALBUM_ART,
                        albumart);
                PlayserSongInfo.putLong(MediaStore.Audio.Media._ID,
                         id);

                PlayerFragment playerFragment = new PlayerFragment();
                playerFragment.setArguments(PlayserSongInfo);
                FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main,playerFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        /*
        String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        ContentResolver content = context.getContentResolver();
        AlbumArtCursor = content.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Audio.Albums._ID+"=?",
                new String[]{songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))},
                null);

        */

        // Log.d(TAG,albumartData.get(songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)))+"");
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

        //Log.d(TAG,"onBindViewHolder Called");
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
            txt_song_name = (TextView) itemView.findViewById(R.id.SongName);
            txt_duration = (TextView) itemView.findViewById(R.id.time);
            txt_album = (TextView) itemView.findViewById(R.id.Album);
            txt_artist = (TextView) itemView.findViewById(R.id.Artist);
            img_AlbumArt = (ImageView) itemView.findViewById(R.id.imageView_AlbumArt);
        }
    }
}