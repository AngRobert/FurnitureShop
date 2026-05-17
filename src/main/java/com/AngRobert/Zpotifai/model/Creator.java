package com.AngRobert.Zpotifai.model;

import java.util.Set;

public abstract class Creator implements Searchable {
    protected int creator_id;
    protected String name;
    protected String description;
    protected Set<Tag> tags;

    public Creator() {}

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
