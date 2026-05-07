package com.AngRobert.Zpotifai.controller;

import com.AngRobert.Zpotifai.App;
import com.AngRobert.Zpotifai.service.SearchService;

import java.util.Scanner;

public class MenuController {

    private final SearchService searchService;

    public MenuController(SearchService searchService) {
        this.searchService = searchService;
    }

    public void start() {
        System.out.println("""
                Welcome to Zpotifai!
                0. Exit.
                1. Search for a creator, album, track or podcast name.
                2. List all tags.
                3. Add a new creator.
                4. Add a new album.
                5. Add a new single.
                6. Add a new podcast.
                7. Add a new song to an album.
                8. Add a new tag to a creator.
                9. Delete a tag from a creator.
                10. Delete a song.
                11. Delete an album.
                12. Delete an artist.
                13. Show database logs.
                """);
        this.handleStartMenuInput();
    }

    public void handleStartMenuInput() {
        Scanner sc = new Scanner(System.in);
        int command = Integer.parseInt(sc.nextLine());

        switch (command) {
            case 1:
                System.out.println("Search: ");
                String input = sc.nextLine();
                searchService.handleSearch(input);
                break;
            default:
                System.out.println("Invalid input\n");
        }
    }
}
