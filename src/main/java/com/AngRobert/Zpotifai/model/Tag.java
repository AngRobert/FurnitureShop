package com.AngRobert.Zpotifai.model;

import java.util.List;

public class Tag {
    private int tag_id;
    private String description;
    private List<Creator> creators;

    public Tag() {}

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Creator> getCreators() {
        return creators;
    }

    public void setCreators(List<Creator> creators) {
        this.creators = creators;
    }
}
