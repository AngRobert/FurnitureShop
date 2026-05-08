package com.AngRobert.Zpotifai.controller;

import com.AngRobert.Zpotifai.service.DatabaseService;
import com.AngRobert.Zpotifai.service.SearchService;

import java.util.Scanner;

public class MenuController {

    private final SearchService searchService;
    private final DatabaseService databaseService;
    private final Scanner scanner;

    public MenuController(SearchService searchService, DatabaseService databaseService, Scanner scanner) {
        this.searchService = searchService;
        this.databaseService = databaseService;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.println("""
                    Welcome to Zpotifai!
                    0. Exit.
                    1. Search for a creator, album, track or podcast name.
                    2. List all tags.
                    3. Add, Update, or Delete a creator.
                    4. Add, Update, or Delete an album.
                    5. Add, Update, or Delete a song.
                    6. Add, Update, or Delete a podcast.
                    7. Add, Update, or Delete a tag.
                    8. Show database logs.
                    """);
            if (!this.handleStartMenuInput()) break;
        }
    }

    public boolean handleStartMenuInput() {
        try {
            int command = Integer.parseInt(scanner.nextLine());

            switch (command) {
                case 0:
                    return false;
                case 1:
                    System.out.print("Search: ");
                    String input = scanner.nextLine();
                    searchService.handleSearch(input);
                    break;
                case 3:
                    handleDbOperation("Creator");
                    break;
                case 4:
                    handleDbOperation("Album");
                    break;
                case 5:
                    handleDbOperation("Song");
                    break;
                case 6:
                    handleDbOperation("Podcast");
                    break;
                case 7:
                    handleDbOperation("Tag");
                    break;
                default:
                    System.out.println("Invalid input");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number from those that appear on screen.");
        }
        return true;
    }

    private void handleDbOperation(String entityType) {
        try {
            System.out.println("""
                Which action would you like to perform?
                0. Back to Menu
                1. Add
                2. Update
                3. Delete
                """);
            int opt = Integer.parseInt(scanner.nextLine());
            switch (opt) {
                case 0:
                    break;
                case 1:
                    handleAddAction(entityType);
                    break;
                case 2:
                    System.out.println("Update logic coming soon...");
                    break;
                case 3:
                    System.out.println("Delete logic coming soon...");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number from those that appear on screen.");
        }
    }

    private void handleAddAction(String entityType) {
        try {
            int result = -1;
            switch (entityType) {
                case "Creator":
                    System.out.println("1. Artist\n2. Host");
                    int type = Integer.parseInt(scanner.nextLine());
                    System.out.print("Creator Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Creator Description: ");
                    String desc = scanner.nextLine();
                    if (type == 1) {
                        System.out.print("Recommended Song: ");
                        String song = scanner.nextLine();
                        result = databaseService.addArtist(name, desc, song);
                    } else {
                        System.out.print("Recommended Podcast: ");
                        String podcast = scanner.nextLine();
                        result = databaseService.addHost(name, desc, podcast);
                    }
                    break;
                case "Podcast":
                    System.out.print("Podcast Name: ");
                    String pName = scanner.nextLine();
                    System.out.print("Podcast Description: ");
                    String pDesc = scanner.nextLine();
                    System.out.print("Podcast Length: ");
                    int pLen = Integer.parseInt(scanner.nextLine());
                    System.out.print("Host Name: ");
                    String hostName = scanner.nextLine();
                    result = databaseService.addPodcast(pName, pDesc, pLen, hostName);
                    break;
                case "Tag":
                    System.out.print("Tag Description: ");
                    String tDesc = scanner.nextLine();
                    result = databaseService.addTag(tDesc);
                    break;
                case "Album":
                    System.out.print("Album Name: ");
                    String aName = scanner.nextLine();
                    System.out.print("Artist Name: ");
                    String albumArtist = scanner.nextLine();
                    result = databaseService.addAlbum(aName, albumArtist);
                    break;
                case "Song":
                    System.out.println("1. Single\n2. Album Track");
                    int sType = Integer.parseInt(scanner.nextLine());
                    System.out.print("Song Name: ");
                    String sName = scanner.nextLine();
                    System.out.print("Song Length: ");
                    int sLen = Integer.parseInt(scanner.nextLine());
                    System.out.print("Artist Name: ");
                    String songArtist = scanner.nextLine();
                    if (sType == 1) {
                        result = databaseService.addSingle(sName, sLen, songArtist);
                    } else {
                        System.out.print("Album Name: ");
                        String albumName = scanner.nextLine();
                        System.out.print("Track Number: ");
                        int trackNum = Integer.parseInt(scanner.nextLine());
                        result = databaseService.addAlbumTrack(sName, sLen, albumName, trackNum, songArtist);
                    }
                    break;
            }
            if (result != -1) {
                System.out.println(entityType + " added successfully!");
            } else {
                System.out.println("Failed to add " + entityType);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }
}
