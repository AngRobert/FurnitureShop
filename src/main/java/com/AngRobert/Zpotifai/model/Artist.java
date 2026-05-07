package com.AngRobert.Zpotifai.model;

import java.util.List;

public class Artist extends Creator {
    private String recommended_song;
    private List<Album> albums;
    private List<Song> songs;

    public Artist() {}

    public String getRecommended_song() {
        return recommended_song;
    }

    public void setRecommended_song(String recommended_song) {
        this.recommended_song = recommended_song;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
