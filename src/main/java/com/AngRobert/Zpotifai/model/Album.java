package com.AngRobert.Zpotifai.model;

import java.time.LocalDate;
import java.util.List;

public class Album {
    private int album_id;
    private String name;
    private LocalDate release_date;
    private List<Artist> artists;
    private List<AlbumTrack> tracks;
}
