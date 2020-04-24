package com.google.blockly.android.webview.utility;

public class Track {
    private String title;
    private String artist;
    private int image;

    public Track(String title, String artist, int image) {
        setTitle(title);
        setArtist(artist);
        setImage(image);
    }

    public Track(String title) {
        this(title, null);
    }

    public Track(String title, String artist) {
        this(title, artist, 0);
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getImage() {
        return image;
    }
}
