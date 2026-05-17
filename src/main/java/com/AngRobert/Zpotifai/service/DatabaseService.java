package com.AngRobert.Zpotifai.service;

import com.AngRobert.Zpotifai.repository.*;

import java.util.List;
import java.util.ArrayList;

public class DatabaseService {
    private final AlbumRepository albumRepository;
    private final AlbumTrackRepository albumTrackRepository;
    private final ArtistRepository artistRepository;
    private final HostRepository hostRepository;
    private final PodcastRepository podcastRepository;
    private final SingleRepository singleRepository;
    private final TagRepository tagRepository;
    private final CollaboratorRepository collaboratorRepository;

    public DatabaseService(AlbumRepository albumRepository, 
                           AlbumTrackRepository albumTrackRepository, 
                           ArtistRepository artistRepository, 
                           HostRepository hostRepository, 
                           PodcastRepository podcastRepository, 
                           SingleRepository singleRepository,
                           TagRepository tagRepository,
                           CollaboratorRepository collaboratorRepository) {
        this.albumRepository = albumRepository;
        this.albumTrackRepository = albumTrackRepository;
        this.artistRepository = artistRepository;
        this.hostRepository = hostRepository;
        this.podcastRepository = podcastRepository;
        this.singleRepository = singleRepository;
        this.tagRepository = tagRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    public int addArtist(String name, String description, String recommendedSong) {
        if (artistRepository.checkDuplicates(name)) {
            System.out.println("Artist with this name already exists!");
            return -1;
        }
        return artistRepository.add(
                List.of("name", "description", "recommended_song"),
                List.of(name, description, recommendedSong)
        );
    }

    public int addHost(String name, String description, String recommendedPodcast) {
        if (hostRepository.checkDuplicates(name)) {
            System.out.println("Host with this name already exists!");
            return -1;
        }
        return hostRepository.add(
                List.of("name", "description", "recommended_podcast"),
                List.of(name, description, recommendedPodcast)
        );
    }

    public int addPodcast(String name, String description, int length, String hostName) {
        if (podcastRepository.checkDuplicates(name)) {
            System.out.println("Podcast with this name already exists!");
            return -1;
        }
        int hostId = hostRepository.getIdByName(hostName);
        if (hostId == -1) {
            System.out.println("Host not found!");
            return -1;
        }

        int podcastId = podcastRepository.add(
                List.of("name", "description", "length"),
                List.of(name, description, length)
        );

        if (podcastId != -1) {
            podcastRepository.add("PODCAST_HOSTS", 
                    List.of("creator_id", "podcast_id"), 
                    List.of(hostId, podcastId));
        }
        return podcastId;
    }

    private List<Integer> getArtistIds(List<String> artistNames) {
        List<Integer> ids = new ArrayList<>();
        for (String name : artistNames) {
            int id = artistRepository.getIdByName(name.trim());
            if (id == -1) {
                System.out.println("Error: Artist '" + name.trim() + "' not found!");
                return null;
            }
            ids.add(id);
        }
        return ids;
    }

    private List<Integer> getCollaboratorIds(List<String> collaboratorNames) {
        if (collaboratorNames.isEmpty()) return new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (String name : collaboratorNames) {
            int id = collaboratorRepository.getIdByName(name.trim());
            if (id == -1) {
                System.out.println("Error: Collaborator '" + name.trim() + "' not found!");
                return null;
            }
            ids.add(id);
        }
        return ids;
    }

    public int addAlbum(String name, List<String> artistNames, String releaseDate) {
        List<Integer> artistIds = getArtistIds(artistNames);
        if (artistIds == null || artistIds.isEmpty()) return -1;

        if (albumRepository.getIdByNameAndArtist(name, artistIds.get(0)) != -1) {
            System.out.println("Album with this name already exists for '" + artistNames.get(0).trim() + "'!");
            return -1;
        }

        int albumId = albumRepository.add(
                List.of("name", "release_date"),
                List.of(name, java.sql.Date.valueOf(releaseDate))
        );

        if (albumId != -1) {
            for (int artistId : artistIds) {
                albumRepository.add("ALBUM_ARTISTS", 
                        List.of("album_id", "creator_id"), 
                        List.of(albumId, artistId));
            }
        }
        return albumId;
    }

    public int addSingle(String name, int length, List<String> artistNames, String releaseDate, List<String> collaboratorNames) {
        List<Integer> artistIds = getArtistIds(artistNames);
        List<Integer> collaboratorIds = getCollaboratorIds(collaboratorNames);

        if (artistIds == null || collaboratorIds == null) return -1;
        if (artistIds.isEmpty()) return -1;

        if (singleRepository.getIdByNameAndArtist(name, artistIds.get(0)) != -1) {
            System.out.println("Single with this name already exists for '" + artistNames.get(0).trim() + "'!");
            return -1;
        }

        int songId = singleRepository.add(
                List.of("name", "length", "release_date"),
                List.of(name, length, java.sql.Date.valueOf(releaseDate))
        );

        if (songId != -1) {
            for (int artistId : artistIds) {
                singleRepository.add("SONG_ARTISTS", 
                        List.of("song_id", "creator_id"), 
                        List.of(songId, artistId));
            }

            for (int colabId : collaboratorIds) {
                collaboratorRepository.add("SONG_COLLABORATIONS",
                        List.of("collaborator_id", "song_id"),
                        List.of(colabId, songId));
            }
        }
        return songId;
    }

    public int addAlbumTrack(String name, int length, String albumName, int trackNumber, List<String> artistNames, List<String> collaboratorNames) {
        List<Integer> artistIds = getArtistIds(artistNames);
        List<Integer> collaboratorIds = getCollaboratorIds(collaboratorNames);

        if (artistIds == null || collaboratorIds == null) return -1;
        if (artistIds.isEmpty()) return -1;

        int albumId = albumRepository.getIdByNameAndArtist(albumName, artistIds.get(0));
        if (albumId == -1) {
            System.out.println("Album not found for artist '" + artistNames.get(0).trim() + "'!");
            return -1;
        }

        if (albumTrackRepository.getIdByNameAndAlbum(name, albumId) != -1) {
            System.out.println("Song with this name already exists on this album!");
            return -1;
        }

        int songId = albumTrackRepository.add(
                List.of("name", "length", "album_id", "track_number"),
                List.of(name, length, albumId, trackNumber)
        );

        if (songId != -1) {
            for (int artistId : artistIds) {
                albumTrackRepository.add("SONG_ARTISTS", 
                        List.of("song_id", "creator_id"), 
                        List.of(songId, artistId));
            }

            for (int collabId : collaboratorIds) {
                collaboratorRepository.add("SONG_COLLABORATIONS",
                        List.of("collaborator_id", "song_id"),
                        List.of(collabId, songId));
            }
        }
        return songId;
    }

    public int addTag(String description) {
        if (tagRepository.checkDuplicates(description)) {
            System.out.println("Tag already exists!");
            return -1;
        }
        return tagRepository.add(List.of("description"), List.of(description));
    }

    public int addTagToCreator(String creatorName, String tagDescription) {
        int creatorId = artistRepository.getIdByName(creatorName);
        if (creatorId == -1) creatorId = hostRepository.getIdByName(creatorName);
        int tagId = tagRepository.getIdByName(tagDescription);
        if (creatorId == -1 || tagId == -1) return -1;
        return tagRepository.add("CREATOR_TAGS", List.of("tag_id", "creator_id"), List.of(tagId, creatorId));
    }

    public int addCollaborator(String name, String description) {
        if (collaboratorRepository.checkDuplicates(name)) {
            System.out.println("Collaborator with this name already exists!");
            return -1;
        }
        return collaboratorRepository.add(List.of("name", "description"), List.of(name, description));
    }

    public int updateArtist(String oldName, String newName, String description, String newRecommendedSong) {
        int id = artistRepository.getIdByName(oldName);
        if (id == -1) return -1;
        return artistRepository.update(id, List.of("name", "description", "recommended_song"), List.of(newName, description, newRecommendedSong));
    }

    public int updateHost(String oldName, String newName, String description, String newRecommendedPodcast) {
        int id = hostRepository.getIdByName(oldName);
        if (id == -1) return -1;
        return hostRepository.update(id, List.of("name", "description", "recommended_podcast"), List.of(newName, description, newRecommendedPodcast));
    }

    public int updatePodcast(String oldName, String newName, String description, int length) {
        int id = podcastRepository.getIdByName(oldName);
        if (id == -1) return -1;
        return podcastRepository.update(id, List.of("name", "description", "length"), List.of(newName, description, length));
    }

    public int updateAlbum(String oldName, String artistName, String newName) {
        int artistId = artistRepository.getIdByName(artistName);
        if (artistId == -1) return -1;
        int albumId = albumRepository.getIdByNameAndArtist(oldName, artistId);
        if (albumId == -1) return -1;
        return albumRepository.update(albumId, List.of("name"), List.of(newName));
    }

    public int updateSingle(String oldName, String artistName, String newName, int length) {
        int artistId = artistRepository.getIdByName(artistName);
        if (artistId == -1) return -1;
        int songId = singleRepository.getIdByNameAndArtist(oldName, artistId);
        if (songId == -1) return -1;
        return singleRepository.update(songId, List.of("name", "length"), List.of(newName, length));
    }

    public int updateAlbumTrack(String oldName, String albumName, String artistName, String newName, int length, int trackNumber) {
        int artistId = artistRepository.getIdByName(artistName);
        if (artistId == -1) return -1;
        int albumId = albumRepository.getIdByNameAndArtist(albumName, artistId);
        if (albumId == -1) return -1;
        int songId = albumTrackRepository.getIdByNameAndAlbum(oldName, albumId);
        if (songId == -1) return -1;
        return albumTrackRepository.update(songId, List.of("name", "length", "track_number"), List.of(newName, length, trackNumber));
    }

    public int updateTag(String oldDescription, String newDescription) {
        int id = tagRepository.getIdByName(oldDescription);
        if (id == -1) return -1;
        return tagRepository.update(id, List.of("description"), List.of(newDescription));
    }

    public int updateCollaborator(String oldName, String newName, String description) {
        int id = collaboratorRepository.getIdByName(oldName);
        if (id == -1) return -1;
        return collaboratorRepository.update(id, List.of("name", "description"), List.of(newName, description));
    }

    public int deleteArtist(String name) {
        int id = artistRepository.getIdByName(name);
        if (id == -1) return -1;
        List<Integer> albumIds = albumRepository.getAlbumIdsByArtistId(id);
        for (int aid : albumIds) deleteAlbumById(aid);
        List<Integer> singleIds = singleRepository.getSongIdsByArtistId(id);
        for (int sid : singleIds) singleRepository.deleteById(sid);
        artistRepository.deleteById(id);
        return id;
    }

    public int deleteHost(String name) {
        int id = hostRepository.getIdByName(name);
        if (id == -1) return -1;
        List<Integer> podcastIds = podcastRepository.getPodcastIdsByHostId(id);
        for (int pid : podcastIds) podcastRepository.deleteById(pid);
        hostRepository.deleteById(id);
        return id;
    }

    public int deletePodcast(String name) {
        int id = podcastRepository.getIdByName(name);
        if (id == -1) return -1;
        podcastRepository.deleteById(id);
        return id;
    }

    private void deleteAlbumById(int albumId) {
        List<Integer> songIds = albumTrackRepository.getSongIdsByAlbumId(albumId);
        for (int sid : songIds) albumTrackRepository.deleteById(sid);
        albumRepository.deleteById(albumId);
    }

    public int deleteAlbum(String name, String artistName) {
        int artistId = artistRepository.getIdByName(artistName);
        if (artistId == -1) return -1;
        int albumId = albumRepository.getIdByNameAndArtist(name, artistId);
        if (albumId == -1) return -1;
        deleteAlbumById(albumId);
        return albumId;
    }

    public int deleteSingle(String name, String artistName) {
        int artistId = artistRepository.getIdByName(artistName);
        if (artistId == -1) return -1;
        int songId = singleRepository.getIdByNameAndArtist(name, artistId);
        if (songId == -1) return -1;
        singleRepository.deleteById(songId);
        return songId;
    }

    public int deleteAlbumTrack(String name, String albumName, String artistName) {
        int artistId = artistRepository.getIdByName(artistName);
        if (artistId == -1) return -1;
        int albumId = albumRepository.getIdByNameAndArtist(albumName, artistId);
        if (albumId == -1) return -1;
        int songId = albumTrackRepository.getIdByNameAndAlbum(name, albumId);
        if (songId == -1) return -1;
        albumTrackRepository.deleteById(songId);
        return songId;
    }

    public int deleteTag(String description) {
        int id = tagRepository.getIdByName(description);
        if (id == -1) return -1;
        tagRepository.deleteById(id);
        return id;
    }

    public int deleteCollaborator(String name) {
        int id = collaboratorRepository.getIdByName(name);
        if (id == -1) return -1;
        collaboratorRepository.deleteById(id);
        return id;
    }

    public Boolean findArtist(String name) { return artistRepository.getIdByName(name) != -1; }

    public Boolean findHost(String name) { return hostRepository.getIdByName(name) != -1; }

    public Boolean findPodcast(String name) { return podcastRepository.getIdByName(name) != -1; }

    public Boolean findAlbum(String albumName, String artistName) {
        int artistId = artistRepository.getIdByName(artistName);
        return artistId != -1 && albumRepository.getIdByNameAndArtist(albumName, artistId) != -1;
    }

    public Boolean findSingle(String songName, String artistName) {
        int artistId = artistRepository.getIdByName(artistName);
        return artistId != -1 && singleRepository.getIdByNameAndArtist(songName, artistId) != -1;
    }

    public Boolean findAlbumTrack(String songName, String albumName, String artistName) {
        int artistId = artistRepository.getIdByName(artistName);
        if (artistId == -1) return false;
        int albumId = albumRepository.getIdByNameAndArtist(albumName, artistId);
        return albumId != -1 && albumTrackRepository.getIdByNameAndAlbum(songName, albumId) != -1;
    }

    public Boolean findTag(String description) { return tagRepository.checkDuplicates(description); }

    public Boolean findCollaborator(String name) { return collaboratorRepository.checkDuplicates(name); }

    public List<String> getAllTags() { return tagRepository.findAll(); }
}
