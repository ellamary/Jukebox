package com.ait.android.jukebox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ait.android.jukebox.QueueActivity;
import com.ait.android.jukebox.R;
import com.ait.android.jukebox.data.Song;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private Context context;
    private final ItemSelectedListener mListener;

    public List<Song> songList;
    public List<String> songKeys;

    public SongAdapter(Context context, ItemSelectedListener listener) {
        this.context = context;
        songList = new ArrayList<>();
        songKeys = new ArrayList<>();
        songsRef = FirebaseDatabase.getInstance().getReference();
        mListener = listener;
    }

    private DatabaseReference songsRef;

    public interface ItemSelectedListener {
        void onItemSelected(View itemView, Track item);
    }

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
        holder.tvScore.setText(Integer.toString(song. getScore()));
        Image image = song.getImage();
        if (image != null) {
            Picasso.with(context).load(image.url).into(holder.ivCoverArt);
        }
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

                if (position != songKeys.size()-1) {
                    int i = 0;

                    while (songList.get(position).getScore() > songList.get(position+1).getScore()) {
                        Song songTemp = songList.get(position);
                        String keyTemp = songKeys.get(position);

                        songList.add(position + i, songTemp);
                        songKeys.add(position + i, keyTemp);

                        songList.remove(position);
                        songKeys.remove(position);

                        i++;
                    }
                }
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

                if (position != 0) {
                    int i = 0;

                    while (songList.get(position).getScore() < songList.get(position-1).getScore()) {
                        Song songTemp = songList.get(position);
                        String keyTemp = songKeys.get(position);

                        songList.add(position - i, songTemp);
                        songKeys.add(position - i, keyTemp);

                        songList.remove(position + 1);
                        songKeys.remove(position + 1);

                        i++;

                    }
                }

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
        songKeys.add(key);
        Song newSong = new Song(track);
        songList.add(newSong);
        notifyDataSetChanged();
    }

    public void addPost(Song song, String key) {
        songKeys.add(key);
        songList.add(song);
        QueueActivity.songsToPlay.add(song);
        notifyDataSetChanged();
    }

    public int identifyPosUpvote(int currentIndex, int compareIndex) {
        Song currentSong = songList.get(currentIndex);
        Song aboveSong = songList.get(compareIndex);
        if (currentSong.getScore() < aboveSong.getScore()) {
            return compareIndex - 1;
        }
        else {
            return identifyPosUpvote(currentIndex, compareIndex - 1);
        }
    }

    public void shiftUpvote(int oldIndex, int newIndex) {

        Song tempSong = songList.get(oldIndex);
        String tempKey = songKeys.get(oldIndex);

        for (int i = oldIndex; i > newIndex; i--) {
            songList.add(i, songList.get(i-1));
            songKeys.add(i, songKeys.get(i-1));
        }

        songList.add(newIndex, tempSong);
        songKeys.add(newIndex, tempKey);
    }

    public int identifyPosDownvote(int currentIndex, int compareIndex) {
        Song currentSong = songList.get(currentIndex);
        Song aboveSong = songList.get(compareIndex);
        if (currentSong.getScore() < aboveSong.getScore()) {
            return compareIndex + 1;
        }
        else {
            return identifyPosUpvote(currentIndex, compareIndex + 1);
        }
    }

    public void shiftDownvote(int oldIndex, int newIndex) {

        Song tempSong = songList.get(oldIndex);
        String tempKey = songKeys.get(oldIndex);

        for (int i = oldIndex; i > newIndex; i++) {
            songList.add(i, songList.get(i+1));
            songKeys.add(i, songKeys.get(i+1));
        }

        songList.add(newIndex, tempSong);
        songKeys.add(newIndex, tempKey);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvTitle;
        public TextView tvArtist;
        public TextView tvScore;
        public ImageView ivCoverArt;
        public ImageButton btnVeto;
        public ImageButton btnUpVote;
        public ImageButton btnDownVote;

        public ViewHolder(View itemView) {
            super(itemView);

            ivCoverArt = itemView.findViewById(R.id.ivCoverArt);
            tvTitle = itemView.findViewById(R.id.entity_title);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvScore = itemView.findViewById(R.id.tvScore);
            btnVeto = itemView.findViewById(R.id.btnVeto);
            btnUpVote = itemView.findViewById(R.id.btnUpVote);
            btnDownVote = itemView.findViewById(R.id.btnDownVote);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(getLayoutPosition());
            mListener.onItemSelected(v, songList.get(getAdapterPosition()).getTrack());
        }
    }
}