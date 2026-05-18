package com.AngRobert.Zpotifai.model;

import java.time.LocalDate;
import java.util.List;

public class Album implements Searchable {
    private int album_id;
    private String name;
    private LocalDate release_date;
    private List<Artist> artists;
    private List<AlbumTrack> tracks;
    private String creatorDisplayName;

    public Album() {}

    @Override
    public String getCreatorDisplayName() {
        return creatorDisplayName != null ? creatorDisplayName : "";
    }

    public void setCreatorDisplayName(String creatorDisplayName) {
        this.creatorDisplayName = creatorDisplayName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public LocalDate getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDate release_date) {
        this.release_date = release_date;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<AlbumTrack> getTracks() {
        return tracks;
    }

    public void setTracks(List<AlbumTrack> tracks) {
        this.tracks = tracks;
    }
}
