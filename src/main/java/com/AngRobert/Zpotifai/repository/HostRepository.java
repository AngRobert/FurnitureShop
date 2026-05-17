package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.Host;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HostRepository extends BaseRepository<Host> implements SearchableRepository<Host>{

    @Override
    public int add(List<String> columns, List<Object> values) {
        return addWithChild("HOSTS", List.of("recommended_podcast"), columns, values);
    }

    @Override
    public int update(int id, List<String> columns, List<Object> values) {
        return updateWithChild("HOSTS", List.of("recommended_podcast"), id, columns, values);
    }

    @Override
    protected Host mapRow(ResultSet rs) throws SQLException {
        Host h = new Host();
        h.setCreator_id(rs.getInt("creator_id"));
        h.setName(rs.getString("name"));
        h.setDescription(rs.getString("description"));
        h.setRecommended_podcast(rs.getString("recommended_podcast"));
        return h;
    }

    @Override
    protected String getTableName() {
        return "HOSTS JOIN CREATORS ON HOSTS.creator_id = CREATORS.creator_id";
    }

    @Override
    protected String getBaseTableName() {
        return "CREATORS";
    }

    @Override
    protected String getIdColumnName() {
        return "creator_id";
    }

    @Override
    public List<Host> searchByName(String name) {
        return searchByColumnName("name", name);
    }

    @Override
    public String getSearchDetails(int id) {
        String baseDetails = super.getSearchDetails(id, List.of("name", "description", "recommended_podcast"));
        StringBuilder details = new StringBuilder(baseDetails);

        List<String> podcasts = getRelatedNames(
                "SELECT P.name FROM PODCASTS P JOIN PODCAST_HOSTS PH ON P.podcast_id = PH.podcast_id WHERE PH.creator_id = ?",
                id
        );
        details.append("Podcasts: ").append(podcasts.isEmpty() ? "None" : String.join(", ", podcasts)).append("\n");

        List<String> tags = getRelatedNames(
                "SELECT T.description FROM TAGS T JOIN CREATOR_TAGS CT ON CT.TAG_ID = T.TAG_ID " +
                        "WHERE CT.CREATOR_ID = ?",
                id
        );
        details.append("Tags: ").append(tags.isEmpty() ? "None" : String.join(", ", tags)).append("\n");

        return details.toString();
    }

    @Override
    public String getCategoryName() {
        return "Hosts";
    }
}
