package in.sensemusic.sense;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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

public class AlbumFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Cursor Media_cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView_Album);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //Log.d("Sense","Album Called");

        String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART,
        };

        ContentResolver content = getActivity().getContentResolver();
        Media_cursor = content.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,//Selection Statement
                null,//Selection Arguments replacement for ? in where id=?
                new String(MediaStore.Audio.Albums.ALBUM+""));

        recyclerView.setAdapter(new AlbumAdapter(getContext(),Media_cursor));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Action Bar title
        ((MainActivity) getActivity()).setActionBarTitle("Albums");
        // ((MainActivity) getActivity()).getSupportActionBar().setTitle("Albums");
    }

}

class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>{

    LayoutInflater layoutInflater;
    Cursor AlbmCursor;
    Context context;

    public AlbumAdapter(Context context,Cursor albmCursor) {
        AlbmCursor = albmCursor;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.album_name,parent,false);
        AlbumViewHolder albumViewHolder = new AlbumViewHolder(view);
        //Log.d("Sense","Album CreateView");
        return albumViewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        if (!AlbmCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        //Log.d("Sense","Album BindView");
        holder.txt_AlbumName.setText(AlbmCursor.getString(AlbmCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
        holder.txt_ArtistName.setText(AlbmCursor.getString(AlbmCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));

        String album_art = AlbmCursor.getString(AlbmCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        //Log.d("Sense",album_art+"");
        Glide
                .with(context)
                .load(album_art)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.album_art)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                )
                .transition(new DrawableTransitionOptions()
                        .crossFade()
                )
                .into(holder.Album_ART)
                ;
    }

    @Override
    public int getItemCount() {
        return AlbmCursor.getCount();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder{

        TextView txt_ArtistName,txt_AlbumName;
        ImageView Album_ART;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            txt_ArtistName = (TextView) itemView.findViewById(R.id.albumArtist);
            txt_AlbumName = (TextView) itemView.findViewById(R.id.AlbumName);
            Album_ART = (ImageView) itemView.findViewById(R.id.imageView_Album_AlbumArt);
        }
    }
}