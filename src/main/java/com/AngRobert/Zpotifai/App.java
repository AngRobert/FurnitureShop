package com.AngRobert.Zpotifai;

import com.AngRobert.Zpotifai.controller.MenuController;
import com.AngRobert.Zpotifai.model.Album;
import com.AngRobert.Zpotifai.model.Podcast;
import com.AngRobert.Zpotifai.repository.*;
import com.AngRobert.Zpotifai.service.SearchService;
import com.AngRobert.Zpotifai.util.DBConnection;
import com.AngRobert.Zpotifai.util.SchemaLoader;

import java.util.List;

public class App {

    private final List<SearchableRepository<?>> repos;

    public App() {
        var albumRepo = new AlbumRepository();
        var albumTrackRepo = new AlbumTrackRepository();
        var artistRepo = new ArtistRepository();
        var hostRepo = new HostRepository();
        var podcastRepo = new PodcastRepository();
        var singleRepo = new SingleRepository();

        this.repos = List.of(albumRepo, albumTrackRepo, artistRepo, hostRepo,
                podcastRepo, singleRepo);
    }

    public void run() {
        DBConnection.init();
        SchemaLoader.run();

        SearchService searchService = new SearchService(this.repos);

        new MenuController(searchService).start();

        DBConnection.close();
    }
}
