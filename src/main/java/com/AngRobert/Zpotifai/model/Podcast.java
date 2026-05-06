package com.AngRobert.Zpotifai.model;

import java.util.List;

public class Podcast extends Streamable {
    private int podcast_id;
    private String name;
    private String description;
    private List<Host> hosts;
}
