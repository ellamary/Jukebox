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
        //song will be added at score = 0; make sure it is added to the end of the arrayList and we will be ok re: sort
    }

    public void upvoteSong(Song song) {
        int index = queue.indexOf(song);
        queue.get(index).upvote();

        if (index != 0) {
            int newIndex = identifyPosUpvote(index, index-1);

            if (index != newIndex) {
                shiftUpvote(index, newIndex);
            }
        }
    }

    public void downvoteSong(Song song) {
        int index = queue.indexOf(song);
        queue.get(index).downvote();
    }

    public int identifyPosUpvote(int currentIndex, int compareIndex) {
        Song currentSong = queue.get(currentIndex);
        Song aboveSong = queue.get(compareIndex);
        if (currentSong.getScore() < aboveSong.getScore()) {
            return compareIndex - 1;
        }
        else {
            return identifyPosUpvote(currentIndex, compareIndex - 1);
        }
    }

    public void shiftUpvote(int oldIndex, int newIndex) {

        Song temp = queue.get(oldIndex);

        for (int i = oldIndex; i > newIndex; i--) {
            queue.add(i, queue.get(i-1));
        }

        queue.add(newIndex, temp);
    }

}
