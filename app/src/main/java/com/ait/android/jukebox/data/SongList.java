package com.ait.android.jukebox.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by ellamary on 12/12/17.
 */

public class SongList {

    private static SongList songList = null;

    private SongList() {

    }

    public static SongList getInstance() {
        if(songList == null) {
            songList = new SongList();
        }

        return songList;
    }

    private List<Song> queue;

    public void init() {
        queue = new ArrayList<Song>();


    }
    public List<Song> getQueue() {
        return this.queue;
    }

    public Song get(int position) {
        Song result = queue.get(position);
        return result;
    }

    public void addSong(Track track) {
        queue.add(new Song(track));
        Log.d("track name", track.name);
        Log.d("tag", "song added");
        sort();
    }

    public void upvoteSong(Song song) {
        int index = queue.indexOf(song);
        queue.get(index).upvote();
    }

    public void downvoteSong(Song song) {
        int index = queue.indexOf(song);
        queue.get(index).downvote();
    }

    public static void sort() {

    }
}
