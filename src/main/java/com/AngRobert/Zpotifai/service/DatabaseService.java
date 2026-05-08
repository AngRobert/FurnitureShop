package com.AngRobert.Zpotifai.service;

import com.AngRobert.Zpotifai.repository.*;

import java.util.List;

public class DatabaseService {
    private final AlbumRepository albumRepository;
    private final AlbumTrackRepository albumTrackRepository;
    private final ArtistRepository artistRepository;
    private final HostRepository hostRepository;
    private final PodcastRepository podcastRepository;
    private final SingleRepository singleRepository;
    private final TagRepository tagRepository;

    public DatabaseService(AlbumRepository albumRepository, 
                           AlbumTrackRepository albumTrackRepository, 
                           ArtistRepository artistRepository, 
                           HostRepository hostRepository, 
                           PodcastRepository podcastRepository, 
                           SingleRepository singleRepository,
                           TagRepository tagRepository) {
        this.albumRepository = albumRepository;
        this.albumTrackRepository = albumTrackRepository;
        this.artistRepository = artistRepository;
        this.hostRepository = hostRepository;
        this.podcastRepository = podcastRepository;
        this.singleRepository = singleRepository;
        this.tagRepository = tagRepository;
    }

    public int addArtist(String name, String description, String recommendedSong) {
        return artistRepository.add(
                List.of("name", "description", "recommended_song"),
                List.of(name, description, recommendedSong)
        );
    }

    public int addHost(String name, String description, String recommendedPodcast) {
        return hostRepository.add(
                List.of("name", "description", "recommended_podcast"),
                List.of(name, description, recommendedPodcast)
        );
    }

    public int addPodcast(String name, String description, int length, String hostName) {
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

    public int addAlbum(String name, String artistName) {
        int artistId = artistRepository.getIdByName(artistName);
        if (artistId == -1) {
            System.out.println("Artist not found!");
            return -1;
        }

        int albumId = albumRepository.add(
                List.of("name"),
                List.of(name)
        );

        if (albumId != -1) {
            albumRepository.add("ALBUM_ARTISTS", 
                    List.of("album_id", "creator_id"), 
                    List.of(albumId, artistId));
        }
        return albumId;
    }

    public int addSingle(String name, int length, String artistName) {
        int artistId = artistRepository.getIdByName(artistName);
        if (artistId == -1) {
            System.out.println("Artist not found!");
            return -1;
        }

        int songId = singleRepository.add(
                List.of("name", "length"),
                List.of(name, length)
        );

        if (songId != -1) {
            singleRepository.add("SONG_ARTISTS", 
                    List.of("song_id", "creator_id"), 
                    List.of(songId, artistId));
        }
        return songId;
    }

    public int addAlbumTrack(String name, int length, String albumName, int trackNumber, String artistName) {
        int artistId = artistRepository.getIdByName(artistName);
        if (artistId == -1) {
            System.out.println("Artist not found!");
            return -1;
        }

        int albumId = albumRepository.getIdByNameAndArtist(albumName, artistId);
        if (albumId == -1) {
            System.out.println("Album not found for this artist!");
            return -1;
        }

        int songId = albumTrackRepository.add(
                List.of("name", "length", "album_id", "track_number"),
                List.of(name, length, albumId, trackNumber)
        );

        if (songId != -1) {
            albumTrackRepository.add("SONG_ARTISTS", 
                    List.of("song_id", "creator_id"), 
                    List.of(songId, artistId));
        }
        return songId;
    }

    public int addTag(String description) {
        return tagRepository.add(
                List.of("description"),
                List.of(description)
        );
    }
}
