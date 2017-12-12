package com.ait.android.jukebox.data;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;

public class Song {
    private String author;
    private String uid;

    private String title;

    private int score;
    private String uri;

    private String artist;

    //using models from wrapper
    private Image image;
    private Track track;


    public Song() {
    }

    public Song(Track track) {
        this.title = track.name;
        List<String> names = new ArrayList<>();
        for (ArtistSimple i : this.getTrack().artists) {
            names.add(i.name);
        }
        Joiner joiner = Joiner.on(", ");
        this.artist = joiner.join(names);
        this.score = 0;
        this.uri = track.uri;
//        this.uid = uid;
        this.image = track.album.images.get(0);
        this.track = track;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public Track getTrack() {
        return track;
    }


    public void setTrack(Track track) {
        this.track = track;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void upvote() { score++; }

    public void downvote() { score--; }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
