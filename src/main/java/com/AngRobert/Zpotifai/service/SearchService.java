package com.AngRobert.Zpotifai.service;

import com.AngRobert.Zpotifai.model.Searchable;
import com.AngRobert.Zpotifai.repository.SearchableRepository;

import java.util.List;

public class SearchService {
    private final List<SearchableRepository<?>> searchableRepositories;

    public SearchService(List<SearchableRepository<?>> repos) {
        this.searchableRepositories = repos;
    }

    // takes the list of repos that appear in the search and calls searchByName for each of them
    public void handleSearch(String name) {
        System.out.println("sunt in handleSearch\n");
        for (SearchableRepository<?> repo : this.searchableRepositories) {
            List<?> results = repo.searchByName(name);
            if (!results.isEmpty()) {
                System.out.println(repo.getCategoryName() + ":\n");
                for (int i = 0; i < results.size(); i ++) {
                    var item = (Searchable) results.get(i);
                    System.out.printf("\t%d: %s%n", i, item.getName());
                }
            }
            System.out.println("\n");
        }
    }
}
