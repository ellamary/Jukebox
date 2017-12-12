package com.ait.android.jukebox.data;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by ellamary on 12/12/17.
 */

public class SongManager {

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

    public void addSong(Track track) {
        queue.add(new Song(track));
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
