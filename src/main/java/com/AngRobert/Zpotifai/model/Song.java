package com.AngRobert.Zpotifai.model;

import java.time.LocalDate;
import java.util.List;

public abstract class Song extends Streamable {
    protected int song_id;
    protected String name;
    protected LocalDate release_date;
    protected List<Collaborator> collaborators;
    protected List<Creator> creators;
}
