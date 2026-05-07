package com.AngRobert.Zpotifai.model;

public class AlbumTrack extends Song {
    private int track_number;
    private Album album;

    public AlbumTrack() {}

    public int getTrack_number() {
        return track_number;
    }

    public void setTrack_number(int track_number) {
        this.track_number = track_number;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
