package com.AngRobert.Zpotifai.service;

import com.AngRobert.Zpotifai.model.Searchable;
import com.AngRobert.Zpotifai.repository.SearchableRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchService {
    private final Map<String, SearchableRepository<?>> repositoryMap;

    public SearchService(List<SearchableRepository<?>> repos) {
        this.repositoryMap = new LinkedHashMap<>();
        for (SearchableRepository<?> repo : repos) {
            this.repositoryMap.put(repo.getCategoryName(), repo);
        }
    }

    // takes the list of repos that appear in the search and calls searchByName for each of them
    public void handleSearch(String name, int category, int entry) {
        int category_no = 1;
        for (SearchableRepository<?> repo : this.repositoryMap.values()) {
            List<?> results = repo.searchByName(name);
            if (!results.isEmpty()) {
                if (category == 0) {
                    // Standard search listing
                    System.out.println(category_no + ": " + repo.getCategoryName() + ":");
                    for (int i = 0; i < results.size(); i++) {
                        var item = (Searchable) results.get(i);
                        System.out.printf("\t%d: %s\n", i + 1, item.getName());
                    }
                    System.out.println();
                } else if (category == category_no) {
                    // Detail retrieval
                    if (entry > 0 && entry <= results.size()) {
                        var item = (Searchable) results.get(entry - 1);
                        System.out.println(repo.getSearchDetails(item.getId()));
                        return;
                    }
                }
                category_no++;
            }
        }
        if (category != 0) {
            System.out.println("Item not found for the given category and number.");
        }
    }

    public void handleSearch(String name) {
        this.handleSearch(name, 0, 0);
    }
}
