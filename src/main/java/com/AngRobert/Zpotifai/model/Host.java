package com.AngRobert.Zpotifai.model;

import java.util.List;

public class Host extends Creator {
    private String recommended_podcast;
    private List<Podcast> podcasts;

    public Host() {}

    public String getRecommended_podcast() {
        return recommended_podcast;
    }

    public void setRecommended_podcast(String recommended_podcast) {
        this.recommended_podcast = recommended_podcast;
    }

    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }
}
