package com.ait.android.jukebox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ait.android.jukebox.R;
import com.ait.android.jukebox.data.Song;
import com.ait.android.jukebox.data.SongList;
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
        Log.d("line","line 37 in song adapter");
        songsRef = FirebaseDatabase.getInstance().getReference();
        Log.d("line","line 39 in song adapter");


    }

    private DatabaseReference songsRef;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row, parent, false);
        Log.d("line","line 49 in on create view holder");
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d("line","line 55 in song adapter");
        final Song song = songList.get(position);
//        Log.d("on bind view holder", song.getTitle());
        holder.tvTitle.setText(song.getTitle());
        holder.tvArtist.setText(song.getArtist());
//        holder.tvScore.setText(Integer.toString(song. getScore()));
//        Image image = song.getImage();
//        if (image != null) {
//            Picasso.with(context).load(image.url).into(holder.ivCoverart);
//        }
        holder.tvScore.setText("" + song.getScore());

        holder.btnVeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePost(holder.getAdapterPosition());
            }
        });

        holder.btnUpVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currKey = songKeys.get(position);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts").child(currKey);
                DatabaseReference scoreReference = reference.child("score");

                int currentScore = song.getScore();
                currentScore++;
                scoreReference.setValue(currentScore);

                songList.get(position).setScore(currentScore);
                holder.tvScore.setText("" + currentScore);

                notifyDataSetChanged();
            }
        });

        holder.btnDownVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currKey = songKeys.get(position);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts").child(currKey);
                DatabaseReference scoreReference = reference.child("score");

                int currentScore = song.getScore();
                currentScore--;
                scoreReference.setValue(currentScore);

                songList.get(position).setScore(currentScore);
                holder.tvScore.setText("" + currentScore);

                notifyDataSetChanged();
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

    public void addPost(Song song, String key) {
        songKeys.add(key);
        songList.add(song);
        notifyDataSetChanged();
    }

    public void updatePost(Song song, String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts").child(key);
        DatabaseReference scoreReference = reference.child("score");


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvArtist;
        public TextView tvScore;
        public Button btnVeto;
        public Button btnUpVote;
        public Button btnDownVote;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.entity_title);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvScore = itemView.findViewById(R.id.tvScore);
            btnVeto = itemView.findViewById(R.id.btnVeto);
            btnUpVote = itemView.findViewById(R.id.btnUpVote);
            btnDownVote = itemView.findViewById(R.id.btnDownVote);
        }
    }
}