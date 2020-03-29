package com.google.blockly.android.webview.utility;

public class Track {
    private String title;
    private String artist;
    private int image;

    public Track(String title, String artist, int image) {
        this.title = title;
        this.artist = artist;
        this.image = image;
    }

    public Track(String title) {
        this.title = title;
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
