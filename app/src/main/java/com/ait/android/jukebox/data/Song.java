package com.ait.android.jukebox.data;

public class Song {

    private String title;
    private String artist;
    private int score;
    private String uri;

    public Song() {}

    public Song(String title, String artist, String uri) {
        this.title = title;
        this.artist = artist;
        this.score = 1;
        this.uri = uri;
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

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
