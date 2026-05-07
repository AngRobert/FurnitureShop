package com.AngRobert.Zpotifai.model;

import java.util.List;

public abstract class Song extends Streamable {
    protected int song_id;
    protected List<Collaborator> collaborators;
    protected List<Creator> creators;

    public Song() {}

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public List<Creator> getCreators() {
        return creators;
    }

    public void setCreators(List<Creator> creators) {
        this.creators = creators;
    }
}
