package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.Podcast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PodcastRepository extends BaseRepository<Podcast> implements SearchableRepository<Podcast>{

    @Override
    protected Podcast mapRow(ResultSet rs) throws SQLException {
        Podcast p = new Podcast();
        p.setPodcast_id(rs.getInt("podcast_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setLength(rs.getInt("length"));
        p.setRelease_date(rs.getDate("release_date").toLocalDate());
        p.setStreams(rs.getInt("streams"));
        return p;
    }

    @Override
    protected String getTableName() {
        return "Podcasts";
    }

    @Override
    protected String getIdColumnName() {
        return "podcast_id";
    }

    @Override
    public List<Podcast> searchByName(String name) {
        return searchByColumnName("name", name);
    }

    @Override
    public String getSearchDetails(int id) {
        String baseDetails = super.getSearchDetails(id, List.of("name", "description", "release_date", "length", "streams"));
        StringBuilder details = new StringBuilder(baseDetails);

        List<String> hosts = getRelatedNames(
                "SELECT C.name FROM CREATORS C JOIN PODCAST_HOSTS PH ON C.creator_id = PH.creator_id WHERE PH.podcast_id = ?",
                id
        );
        details.append("Hosts: ").append(hosts.isEmpty() ? "None" : String.join(", ", hosts)).append("\n");

        return details.toString();
    }

    @Override
    public String getCategoryName() {
        return "Podcasts";
    }
}
