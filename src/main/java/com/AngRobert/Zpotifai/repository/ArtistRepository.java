package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.Artist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ArtistRepository extends BaseRepository<Artist> implements SearchableRepository<Artist> {

    @Override
    public int add(List<String> columns, List<Object> values) {
        return addWithChild("ARTISTS", List.of("recommended_song"), columns, values);
    }

    @Override
    protected Artist mapRow(ResultSet rs) throws SQLException {
        Artist a = new Artist();
        a.setCreator_id(rs.getInt("creator_id"));
        a.setName(rs.getString("name"));
        a.setDescription(rs.getString("description"));
        a.setRecommended_song(rs.getString("recommended_song"));
        return a;
    }

    @Override
    protected String getTableName() {
        return "ARTISTS JOIN CREATORS ON ARTISTS.creator_id = CREATORS.creator_id";
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
    public List<Artist> searchByName(String name) {
        return this.searchByColumnName("name", name);
    }

    @Override
    public String getCategoryName() {
        return "Artists";
    }
}
