package com.AngRobert.Zpotifai.model;

import java.util.List;

public class Podcast extends Streamable {
    private int podcast_id;
    private String description;
    private List<Host> hosts;

    public Podcast() {}

    public int getPodcast_id() {
        return podcast_id;
    }

    public void setPodcast_id(int podcast_id) {
        this.podcast_id = podcast_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }
}
