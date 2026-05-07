package com.AngRobert.Zpotifai.repository;

import java.util.List;

public interface SearchableRepository<T> {

    public List<T> searchByName(String name);

    // for printing purposes
    public String getCategoryName();
}
