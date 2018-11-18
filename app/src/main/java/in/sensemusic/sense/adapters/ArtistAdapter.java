package in.sensemusic.sense.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
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

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>{

    private Cursor artistCursor;
    private LayoutInflater layoutInflater;
    private Context context;
    private HashMap<String,String> albumartData;

    public ArtistAdapter(Context context, Cursor artistCursor) {
        this.artistCursor = artistCursor;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        albumartData = new HashMap<>();

        String[] projection = {
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        ContentResolver content = context.getContentResolver();
        try (Cursor albumArtCursor = content.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null, //new String[]{songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))},
                null)) {

            while (Objects.requireNonNull(albumArtCursor).moveToNext()) {
                albumartData.put(albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)),
                        albumArtCursor.getString(albumArtCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
                //   Log.d(TAG,albumartData.get(AlbumArtCursor.getString(AlbumArtCursor.getColumnIndex(MediaStore.Audio.Albums._ID)))+"Albumdata");
            }
        }
    }


    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.artist_name,parent,false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        if (!artistCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        holder.txt_artistName.setText(artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
        holder.txt_no_of_songs.setText(String.format("%s Songs", artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))));

        Glide
                .with(context)
                .load(albumartData.get(artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.album_art)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                )
                .thumbnail(0.5f)
                .transition(new DrawableTransitionOptions()
                        .crossFade()
                )
                .into(holder.img_ArtistAlbumArt);
    }

    @Override
    public int getItemCount() {
        return artistCursor.getCount();
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder{

        TextView txt_artistName,txt_no_of_songs;
        ImageView img_ArtistAlbumArt;
        ArtistViewHolder(View itemView) {
            super(itemView);
            txt_artistName = itemView.findViewById(R.id.artistName);
            txt_no_of_songs = itemView.findViewById(R.id.no_of_songs);
            img_ArtistAlbumArt = itemView.findViewById(R.id.imageView_Artist_AlbumArt);
        }
    }
}