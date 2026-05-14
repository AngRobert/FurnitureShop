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
    public int update(int id, List<String> columns, List<Object> values) {
        return updateWithChild("ARTISTS", List.of("recommended_song"), id, columns, values);
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
    public String getSearchDetails(int id) {
        String baseDetails = super.getSearchDetails(id, List.of("name", "description", "recommended_song"));
        StringBuilder details = new StringBuilder(baseDetails);

        List<String> albums = getRelatedNames(
                "SELECT A.name FROM ALBUMS A JOIN ALBUM_ARTISTS AA ON A.album_id = AA.album_id WHERE AA.creator_id = ?",
                id
        );
        details.append("Albums: ").append(albums.isEmpty() ? "None" : String.join(", ", albums)).append("\n");

        List<String> singles = getRelatedNames(
                "SELECT S.name FROM SONGS S JOIN SONG_ARTISTS SA ON S.song_id = SA.song_id " +
                        "JOIN SINGLES SI ON S.song_id = SI.song_id WHERE SA.creator_id = ?",
                id
        );
        details.append("Singles: ").append(singles.isEmpty() ? "None" : String.join(", ", singles)).append("\n");

        return details.toString();
    }

    @Override
    public String getCategoryName() {
        return "Artists";
    }
}
