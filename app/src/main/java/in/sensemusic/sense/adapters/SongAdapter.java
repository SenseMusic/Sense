package in.sensemusic.sense.adapters;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
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
import java.util.Objects;

import in.sensemusic.sense.R;
import in.sensemusic.sense.fragments.PlayerFragment;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{
    private Cursor songCursor;
    private Context context;
    private LayoutInflater layoutInflater;
    private HashMap<String, String> albumartData;
    private Bundle PlayerSongInfo  = new Bundle();

    public SongAdapter(Context context, Cursor songCursor) {
        this.songCursor = songCursor;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);

        albumartData = new HashMap<>();

        String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        ContentResolver content = context.getContentResolver();
        @SuppressLint("Recycle") Cursor albumArtCursor = content.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null, //new String[]{songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))},
                null);

        while(Objects.requireNonNull(albumArtCursor).moveToNext())
        {
            albumartData.put(albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums._ID)),
                    albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
            //   Log.d(TAG,albumartData.get(AlbumArtCursor.getString(AlbumArtCursor.getColumnIndex(MediaStore.Audio.Albums._ID)))+"Albumdata");
        }
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d(TAG,"called OnCreateViewHolder()");
        View view = layoutInflater.inflate(R.layout.song_name,parent,false);
        return new SongViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

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

        holder.txt_song_name.setOnClickListener(v -> {
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

        SongViewHolder(View itemView) {
            super(itemView);
            txt_song_name = itemView.findViewById(R.id.SongName);
            txt_duration = itemView.findViewById(R.id.time);
            txt_album = itemView.findViewById(R.id.Album);
            txt_artist = itemView.findViewById(R.id.Artist);
            img_AlbumArt = itemView.findViewById(R.id.imageView_AlbumArt);
        }
    }
}
