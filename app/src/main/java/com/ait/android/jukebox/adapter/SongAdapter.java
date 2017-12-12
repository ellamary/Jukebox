package com.ait.android.jukebox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ait.android.jukebox.R;
import com.ait.android.jukebox.data.Song;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{

    private Context context;
    public List<Song> songList;
    public List<String> songKeys;
    private int lastPosition = -1;

    public SongAdapter(Context context) {
        this.context = context;
        songList = new ArrayList<Song>();
        songKeys = new ArrayList<String>();

        songsRef = FirebaseDatabase.getInstance().getReference();
    }

    private DatabaseReference songsRef;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Song song = songList.get(position);

        holder.tvTitle.setText(song.getTitle());
        holder.tvArtist.setText(song.getArtist());
//        holder.tvScore.setText(Integer.toString(song.getScore()));
//        Image image = song.getImage();
//        if (image != null) {
//            Picasso.with(context).load(image.url).into(holder.ivCoverart);
//        }
        holder.btnVeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePost(holder.getAdapterPosition());
            }
        });

        holder.btnUpVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                song.setScore((song.getScore())+1);
                notifyDataSetChanged();
            }
        });

        holder.btnDownVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (song.getScore() > 0) {
                    song.setScore((song.getScore()) - 1);
                    notifyDataSetChanged();
                }
            }
        });

    }

    public void removePost(int index) {
        songsRef.child("posts").child(songKeys.get(index)).removeValue();
        songList.remove(index);
        songKeys.remove(index);
        notifyItemRemoved(index);
    }

    public void removePostByKey(String key) {
        int index = songKeys.indexOf(key);
        if (index != -1) {
            songList.remove(index);
            songKeys.remove(index);
            notifyItemRemoved(index);
    }
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void addPost(Track track, String key) {
//        songList.add(song);
        songKeys.add(key);
//        trackList.add(track);
        Song newSong = new Song(track);
        songList.add(newSong);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvArtist;
        public TextView tvScore;
        public TextView tvAuthor;
        public Button btnVeto;
        public Button btnUpVote;
        public Button btnDownVote;
        public ImageView ivCoverart;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.entity_title);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            btnVeto = itemView.findViewById(R.id.btnVeto);
            ivCoverart = itemView.findViewById(R.id.entity_image);
            btnUpVote = itemView.findViewById(R.id.btnUpVote);
            btnDownVote = itemView.findViewById(R.id.btnDownVote);
        }
    }
}