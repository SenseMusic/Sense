package in.sensemusic.sense.fragments;

import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.Objects;

import in.sensemusic.sense.R;
import in.sensemusic.sense.activities.MainActivity;

public class PlayerFragment extends Fragment {

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle SongInfo = getArguments();

        TextView NowPlayingTrack,Album,Artist,Duration;
        ImageView AlbumArt;

        NowPlayingTrack = Objects.requireNonNull(getActivity()).findViewById(R.id.song);
        Album = getActivity().findViewById(R.id.album);
        Artist = getActivity().findViewById(R.id.artist);
        Duration = getActivity().findViewById(R.id.time_duration);
        AlbumArt = getActivity().findViewById(R.id.album_art);


        if (SongInfo != null) {
            long currentSongID = SongInfo.getLong(MediaStore.Audio.Media._ID);
            NowPlayingTrack.setText(SongInfo.getString(MediaStore.Audio.Media.TITLE));
            Artist.setText(SongInfo.getString(MediaStore.Audio.Media.ARTIST));
            Album.setText(SongInfo.getString(MediaStore.Audio.Media.ALBUM));
            Duration.setText(SongInfo.getString(MediaStore.Audio.Media.DURATION));

            Glide
                    .with(Objects.requireNonNull(getContext()))
                    .load(SongInfo.getString(MediaStore.Audio.Albums.ALBUM_ART))
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.album_art)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    )
                    .transition(new DrawableTransitionOptions()
                            .crossFade()
                    )
                    .into(AlbumArt);

            //NOTE: Not calling this function as in ArtistsActivity we are using player service to play song
            //playSongWithoutService(currentSongID);
        }
        else {
            NowPlayingTrack.setText(getString(R.string.defaultSongTitle));
            Artist.setText(getString(R.string.defaultSongArtist));
            Album.setText(getString(R.string.defaultSongAlbum));
            Duration.setText(getString(R.string.defaultSongDuration));

            Glide
                    .with(Objects.requireNonNull(getContext())).load("")
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.album_art)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    )
                    .transition(new DrawableTransitionOptions()
                            .crossFade()
                    )
                    .into(AlbumArt);

        }
       // Log.e("Sense", currentSongID+" currentSongID");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Action Bar title
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle("Player");
        // ((MainActivity) getActivity()).getSupportActionBar().setTitle("Player");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Stop Play when paused
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void playSongWithoutService(long currSong){

        // prepare uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        //set player properties
        mediaPlayer.setWakeMode(Objects.requireNonNull(getActivity()).getApplicationContext(),PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //play a song from start
        mediaPlayer.reset();

        //set source
        try{
            mediaPlayer.setDataSource(getActivity().getApplicationContext(), trackUri);
        } catch(Exception e){
            Log.e("sense", "Error setting data source", e);
        }

        //prepare
        try{
            mediaPlayer.prepare();
        }catch(IOException e){
            Log.e("sense", "Error at media player prepare", e);}

        //start play
        mediaPlayer.start();
    }

}
