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
                    handleUpdateAction(entityType);
                    break;
                case 3:
                    System.out.println("Delete logic coming soon...");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number from those that appear on screen.");
        }
    }

    private void handleUpdateAction(String entityType) {
        try {
            int result = -1;
            switch (entityType) {
                case "Creator":
                    System.out.println("1. Artist\n2. Host");
                    int type = Integer.parseInt(scanner.nextLine());
                    if (type == 1) {
                        System.out.println("Artist name: ");
                        String name = scanner.nextLine();
                        if (!databaseService.findArtist(name)) {
                            System.out.println("Error: Artist '" + name + "' not found!");
                            return;
                        }

                        System.out.println("New artist name: ");
                        String newName = scanner.nextLine();
                        System.out.println("New artist description: ");
                        String newDescription = scanner.nextLine();
                        System.out.println("New recommended song: ");
                        String newRecommendedSong = scanner.nextLine();
                        result = databaseService.updateArtist(name, newName, newDescription, newRecommendedSong);
                    } else {
                        System.out.println("Host name: ");
                        String name = scanner.nextLine();
                        if (!databaseService.findHost(name)) {
                            System.out.println("Error: Host '" + name + "' not found!");
                            return;
                        }

                        System.out.println("New host name: ");
                        String newName = scanner.nextLine();
                        System.out.println("New host description: ");
                        String newDescription = scanner.nextLine();
                        System.out.println("New recommended podcast: ");
                        String newRecommendedPodcast = scanner.nextLine();
                        result = databaseService.updateHost(name, newName, newDescription, newRecommendedPodcast);
                    }
                    break;

                case "Podcast":
                    System.out.print("Podcast Name: ");
                    String pName = scanner.nextLine();
                    if (!databaseService.findPodcast(pName)) {
                        System.out.println("Error: Podcast '" + pName + "' not found!");
                        return;
                    }

                    System.out.print("New Podcast Name: ");
                    String newPName = scanner.nextLine();
                    System.out.print("New Podcast Description: ");
                    String newPDesc = scanner.nextLine();
                    System.out.print("New Podcast Length: ");
                    int newPLen = Integer.parseInt(scanner.nextLine());
                    result = databaseService.updatePodcast(pName, newPName, newPDesc, newPLen);
                    break;

                case "Album":
                    System.out.print("Artist Name: ");
                    String albumArtist = scanner.nextLine();
                    if (!databaseService.findArtist(albumArtist)) {
                        System.out.println("Error: Artist '" + albumArtist + "' not found!");
                        return;
                    }

                    System.out.print("Album Name: ");
                    String aName = scanner.nextLine();
                    if (!databaseService.findAlbum(aName, albumArtist)) {
                        System.out.println("Error: Album '" + aName + "' not found for artist '" + albumArtist + "'!");
                        return;
                    }

                    System.out.print("New Album Name: ");
                    String newAName = scanner.nextLine();
                    result = databaseService.updateAlbum(aName, albumArtist, newAName);
                    break;

                case "Song":
                    System.out.println("1. Single\n2. Album Track");
                    int sType = Integer.parseInt(scanner.nextLine());
                    
                    System.out.print("Artist Name: ");
                    String songArtist = scanner.nextLine();
                    if (!databaseService.findArtist(songArtist)) {
                        System.out.println("Error: Artist '" + songArtist + "' not found!");
                        return;
                    }

                    if (sType == 1) {
                        System.out.print("Single Name: ");
                        String sName = scanner.nextLine();
                        if (!databaseService.findSingle(sName, songArtist)) {
                            System.out.println("Error: Single '" + sName + "' not found for artist '" + songArtist + "'!");
                            return;
                        }
                        System.out.print("New Song Name: ");
                        String newSName = scanner.nextLine();
                        System.out.print("New Song Length: ");
                        int newSLen = Integer.parseInt(scanner.nextLine());
                        result = databaseService.updateSingle(sName, songArtist, newSName, newSLen);
                    } else {
                        System.out.print("Album Name: ");
                        String albumName = scanner.nextLine();
                        if (!databaseService.findAlbum(albumName, songArtist)) {
                            System.out.println("Error: Album '" + albumName + "' not found for artist '" + songArtist + "'!");
                            return;
                        }

                        System.out.print("Song Name: ");
                        String sName = scanner.nextLine();
                        if (!databaseService.findAlbumTrack(sName, albumName, songArtist)) {
                            System.out.println("Error: Song '" + sName + "' not found on album '" + albumName + "'!");
                            return;
                        }

                        System.out.print("New Song Name: ");
                        String newSName = scanner.nextLine();
                        System.out.print("New Song Length: ");
                        int newSLen = Integer.parseInt(scanner.nextLine());
                        System.out.print("New Track Number: ");
                        int newTrackNum = Integer.parseInt(scanner.nextLine());
                        result = databaseService.updateAlbumTrack(sName, albumName, songArtist, newSName, newSLen, newTrackNum);
                    }
                    break;

                case "Tag":
                    System.out.print("Tag Description: ");
                    String tDesc = scanner.nextLine();
                    if (!databaseService.findTag(tDesc)) {
                        System.out.println("Error: Tag '" + tDesc + "' not found!");
                        return;
                    }

                    System.out.print("New Tag Description: ");
                    String newTDesc = scanner.nextLine();
                    result = databaseService.updateTag(tDesc, newTDesc);
                    break;
            }

            if (result != -1) {
                System.out.println(entityType + " updated successfully!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter numbers where requested.");
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
