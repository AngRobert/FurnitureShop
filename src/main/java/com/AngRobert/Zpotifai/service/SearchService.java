package com.AngRobert.Zpotifai.service;

import com.AngRobert.Zpotifai.model.Searchable;
import com.AngRobert.Zpotifai.repository.SearchableRepository;

import java.util.*;

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
            List<?> rawResults = repo.searchByName(name);
            if (!rawResults.isEmpty()) {
                // Use a TreeSet to satisfy the "sorted collection" requirement
                TreeSet<Searchable> sortedResults = new TreeSet<>();
                for (Object obj : rawResults) {
                    sortedResults.add((Searchable) obj);
                }
                
                // Convert back to list for index-based access while maintaining the new sort order
                List<Searchable> results = new ArrayList<>(sortedResults);

                if (category == 0) {
                    // Standard search listing
                    System.out.println(category_no + ": " + repo.getCategoryName() + ":");
                    for (int i = 0; i < results.size(); i++) {
                        System.out.printf("\t%d: %s\n", i + 1, results.get(i).getName());
                    }
                    System.out.println();
                } else if (category == category_no) {
                    // Detail retrieval
                    if (entry > 0 && entry <= results.size()) {
                        Searchable item = results.get(entry - 1);
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
