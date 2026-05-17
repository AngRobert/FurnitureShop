package com.AngRobert.Zpotifai.controller;

import com.AngRobert.Zpotifai.service.DatabaseService;
import com.AngRobert.Zpotifai.service.SearchService;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

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
                    8. Add a tag to a creator.
                    9. Add, Update, or Delete a collaborator.
                    10. Show database logs.
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
                    System.out.println("To see more details about an entry, enter the category number followed by the entry number (e.g., '1 1').");
                    System.out.println("Otherwise, press Enter to return to the main menu.");
                    String detailInput = scanner.nextLine();
                    if (!detailInput.trim().isEmpty()) {
                        String[] parts = detailInput.split("\\s+");
                        if (parts.length == 2) {
                            try {
                                int category = Integer.parseInt(parts[0]);
                                int entry = Integer.parseInt(parts[1]);
                                searchService.handleSearch(input, category, entry);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid format. Please enter two numbers.");
                            }
                        }
                    }
                    break;
                case 2:
                    listAllTags();
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
                case 8:
                    handleTagAssociations();
                    break;
                case 9:
                    handleDbOperation("Collaborator");
                    break;
                default:
                    System.out.println("Invalid input");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number from those that appear on screen.");
        }
        return true;
    }

    private void listAllTags() {
        List<String> tags = new ArrayList<>();
        tags = databaseService.getAllTags();
        System.out.println("Tags: ");
        for (int i = 0; i < tags.size(); i ++) {
            if (i != tags.size() - 1) {
                System.out.printf("%s, ", tags.get(i));
            }
            else {
                System.out.println(tags.get(i) + "\n");
            }
        }
    }

    private void handleTagAssociations() {
        System.out.println("1. Artist\n2. Host");
        int type = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Creator Name: ");
        String cName = scanner.nextLine();

        if (type == 1 && !databaseService.findArtist(cName)) {
            System.out.println("Error: Artist '" + cName + "' not found!");
            return;
        } else if (type == 2 && !databaseService.findHost(cName)) {
            System.out.println("Error: Host '" + cName + "' not found!");
            return;
        }

        System.out.print("Tag name: ");
        String tName = scanner.nextLine();

        if (!databaseService.findTag(tName)) {
            System.out.println("Error: Tag '" + tName + "' not found!");
            return;
        }

        int result = databaseService.addTagToCreator(cName, tName);
        if (result != -1) {
            System.out.println("Tag associated successfully with creator!");
        }
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
                    handleDeleteAction(entityType);
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number from those that appear on screen.");
        }
    }

    private void handleDeleteAction(String entityType) {
        try {
            int result = -1;
            switch (entityType) {
                case "Creator":
                    System.out.println("1. Artist\n2. Host");
                    int type = Integer.parseInt(scanner.nextLine());
                    if (type == 1) {
                        System.out.print("Artist Name to delete: ");
                        String name = scanner.nextLine();
                        if (!databaseService.findArtist(name)) {
                            System.out.println("Error: Artist '" + name + "' not found!");
                            return;
                        }
                        result = databaseService.deleteArtist(name);
                    } else {
                        System.out.print("Host Name to delete: ");
                        String name = scanner.nextLine();
                        if (!databaseService.findHost(name)) {
                            System.out.println("Error: Host '" + name + "' not found!");
                            return;
                        }
                        result = databaseService.deleteHost(name);
                    }
                    break;

                case "Podcast":
                    System.out.print("Podcast Name to delete: ");
                    String pName = scanner.nextLine();
                    if (!databaseService.findPodcast(pName)) {
                        System.out.println("Error: Podcast '" + pName + "' not found!");
                        return;
                    }
                    result = databaseService.deletePodcast(pName);
                    break;

                case "Album":
                    System.out.print("Artist Name: ");
                    String albumArtist = scanner.nextLine();
                    if (!databaseService.findArtist(albumArtist)) {
                        System.out.println("Error: Artist '" + albumArtist + "' not found!");
                        return;
                    }

                    System.out.print("Album Name to delete: ");
                    String aName = scanner.nextLine();
                    if (!databaseService.findAlbum(aName, albumArtist)) {
                        System.out.println("Error: Album '" + aName + "' not found for artist '" + albumArtist + "'!");
                        return;
                    }
                    result = databaseService.deleteAlbum(aName, albumArtist);
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
                        System.out.print("Single Name to delete: ");
                        String sName = scanner.nextLine();
                        if (!databaseService.findSingle(sName, songArtist)) {
                            System.out.println("Error: Single '" + sName + "' not found for artist '" + songArtist + "'!");
                            return;
                        }
                        result = databaseService.deleteSingle(sName, songArtist);
                    } else {
                        System.out.print("Album Name: ");
                        String albumName = scanner.nextLine();
                        if (!databaseService.findAlbum(albumName, songArtist)) {
                            System.out.println("Error: Album '" + albumName + "' not found for artist '" + songArtist + "'!");
                            return;
                        }

                        System.out.print("Song Name to delete: ");
                        String sName = scanner.nextLine();
                        if (!databaseService.findAlbumTrack(sName, albumName, songArtist)) {
                            System.out.println("Error: Song '" + sName + "' not found on album '" + albumName + "'!");
                            return;
                        }
                        result = databaseService.deleteAlbumTrack(sName, albumName, songArtist);
                    }
                    break;

                case "Tag":
                    System.out.print("Tag Description to delete: ");
                    String tDesc = scanner.nextLine();
                    if (!databaseService.findTag(tDesc)) {
                        System.out.println("Error: Tag '" + tDesc + "' not found!");
                        return;
                    }
                    result = databaseService.deleteTag(tDesc);
                    break;
                case "Collaborator":
                    System.out.print("Collaborator Name to delete: ");
                    String collName = scanner.nextLine();
                    if (!databaseService.findCollaborator(collName)) {
                        System.out.println("Error: Collaborator '" + collName + "' not found!");
                        return;
                    }
                    result = databaseService.deleteCollaborator(collName);
                    break;
            }

            if (result != -1) {
                System.out.println(entityType + " deleted successfully!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
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
                case "Collaborator":
                    System.out.print("Collaborator Name: ");
                    String colName = scanner.nextLine();
                    if (!databaseService.findCollaborator(colName)) {
                        System.out.println("Error: Collaborator '" + colName + "' not found!");
                        return;
                    }
                    System.out.print("New Collaborator Name: ");
                    String newColName = scanner.nextLine();
                    System.out.print("New Collaborator Description: ");
                    String newColDesc = scanner.nextLine();
                    result = databaseService.updateCollaborator(colName, newColName, newColDesc);
                    break;
            }

            if (result != -1) {
                System.out.println(entityType + " updated successfully!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter numbers where requested.");
        }
    }

    private String readValidDate(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            try {
                java.time.LocalDate.parse(input);
                return input;
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    private String readNonEmptyString(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Error: Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    private void handleAddAction(String entityType) {
        try {
            int result = -1;
            switch (entityType) {
                case "Creator":
                    System.out.println("1. Artist\n2. Host");
                    int type = Integer.parseInt(scanner.nextLine());
                    String name = readNonEmptyString("Creator Name: ");
                    String desc = readNonEmptyString("Creator Description: ");
                    if (type == 1) {
                        String song = readNonEmptyString("Recommended Song: ");
                        result = databaseService.addArtist(name, desc, song);
                    } else {
                        String podcast = readNonEmptyString("Recommended Podcast: ");
                        result = databaseService.addHost(name, desc, podcast);
                    }
                    break;
                case "Podcast":
                    String pName = readNonEmptyString("Podcast Name: ");
                    String pDesc = readNonEmptyString("Podcast Description: ");
                    System.out.print("Podcast Length (seconds): ");
                    int pLen = Integer.parseInt(scanner.nextLine());
                    String hostName = readNonEmptyString("Host Name: ");
                    result = databaseService.addPodcast(pName, pDesc, pLen, hostName);
                    break;
                case "Tag":
                    String tDesc = readNonEmptyString("Tag Description: ");
                    result = databaseService.addTag(tDesc);
                    break;
                case "Album":
                    String aName = readNonEmptyString("Album Name: ");
                    String artistInput = readNonEmptyString("Artist Names (comma separated): ");
                    List<String> albumArtists = List.of(artistInput.split(","));
                    String aReleaseDate = readValidDate("Release Date (YYYY-MM-DD): ");
                    result = databaseService.addAlbum(aName, albumArtists, aReleaseDate);
                    break;
                case "Song":
                    System.out.println("1. Single\n2. Album Track");
                    int sType = Integer.parseInt(scanner.nextLine());
                    String sName = readNonEmptyString("Song Name: ");
                    System.out.print("Song Length (seconds): ");
                    int sLen = Integer.parseInt(scanner.nextLine());
                    String songArtistInput = readNonEmptyString("Artist Names (comma separated): ");
                    List<String> songArtists = List.of(songArtistInput.split(","));
                    if (sType == 1) {
                        String sReleaseDate = readValidDate("Release Date (YYYY-MM-DD): ");
                        String collabsInput = readNonEmptyString("Collaborator Names (comma separated, or 'none'): ");
                        List<String> collabs = collabsInput.equalsIgnoreCase("none") ? List.of() : List.of(collabsInput.split(","));
                        result = databaseService.addSingle(sName, sLen, songArtists, sReleaseDate, collabs);
                    } else {
                        String albumName = readNonEmptyString("Album Name: ");
                        System.out.print("Track Number: ");
                        int trackNum = Integer.parseInt(scanner.nextLine());
                        String collabsInput = readNonEmptyString("Collaborator Names (comma separated, or 'none'): ");
                        List<String> collabs = collabsInput.equalsIgnoreCase("none") ? List.of() : List.of(collabsInput.split(","));
                        result = databaseService.addAlbumTrack(sName, sLen, albumName, trackNum, songArtists, collabs);
                    }
                    break;
                case "Collaborator":
                    String collName = readNonEmptyString("Collaborator Name: ");
                    String collDesc = readNonEmptyString("Collaborator Description: ");
                    result = databaseService.addCollaborator(collName, collDesc);
                    break;
            }
            if (result != -1) {
                System.out.println(entityType + " added successfully!");
            } else {
                System.out.println("Failed to add " + entityType);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter numbers where requested.");
        }
    }
}
