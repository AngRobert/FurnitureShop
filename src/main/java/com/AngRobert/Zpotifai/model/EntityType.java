package com.AngRobert.Zpotifai.model;

public enum EntityType {
    CREATOR("Creator"),
    ALBUM("Album"),
    SONG("Song"),
    PODCAST("Podcast"),
    TAG("Tag"),
    COLLABORATOR("Collaborator");

    private final String displayName;

    EntityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
