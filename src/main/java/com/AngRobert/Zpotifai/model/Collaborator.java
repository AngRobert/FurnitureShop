package com.AngRobert.Zpotifai.model;

import java.util.List;

public class Collaborator {
    private int collaborator_id;
    private String name;
    private String description;
    private List<Song> songs;

    public Collaborator() {}

    public int getCollaborator_id() {
        return collaborator_id;
    }

    public void setCollaborator_id(int collaborator_id) {
        this.collaborator_id = collaborator_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
