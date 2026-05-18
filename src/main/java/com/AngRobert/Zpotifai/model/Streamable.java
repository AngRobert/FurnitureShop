package com.AngRobert.Zpotifai.model;

import java.time.LocalDate;

public abstract class Streamable implements Searchable {
    protected String name;
    protected LocalDate release_date;
    protected int length;
    protected int streams;
    protected String creatorDisplayName;

    public Streamable() {}

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

    public LocalDate getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDate release_date) {
        this.release_date = release_date;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStreams() {
        return streams;
    }

    public void setStreams(int streams) {
        this.streams = streams;
    }
}
