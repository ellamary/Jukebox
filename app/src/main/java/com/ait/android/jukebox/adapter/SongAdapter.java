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
//import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{

    private Context context;
    private List<Song> songList;
    private List<String> songKeys;
    private String uId;
    private int lastPosition = -1;

    public SongAdapter(Context context, String uId) {
        this.context = context;
        this.uId = uId;

        songList = new ArrayList<Song>();
        songKeys = new ArrayList<String>();

        postsRef = FirebaseDatabase.getInstance().getReference();
    }

    private DatabaseReference postsRef;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Song song = songList.get(position);

        holder.tvTitle.setText(song.getTitle());
        holder.tvArtist.setText(song.getArtist());
        holder.tvScore.setText(song.getScore());

        holder.btnVeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePost(holder.getAdapterPosition());


            }
        });

    }

    public void removePost(int index) {
        postsRef.child("posts").child(songKeys.get(index)).removeValue();
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

    public void addPost(Song song, String key) {
        songList.add(song);
        songKeys.add(key);

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvArtist;
        public TextView tvScore;
        public Button btnVeto;
        public Button btnUp;
        public Button btnDown;
        public ImageView ivPostImg;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvScore = itemView.findViewById(R.id.tvScore);
            btnVeto = itemView.findViewById(R.id.btnVeto);
            ivPostImg = itemView.findViewById(R.id.ivPostImg);
            btnUp = itemView.findViewById(R.id.btnUp);
            btnDown = itemView.findViewById(R.id.btnDown);
        }
    }
}
