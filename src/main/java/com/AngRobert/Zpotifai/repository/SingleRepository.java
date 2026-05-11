package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.Single;
import com.AngRobert.Zpotifai.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SingleRepository extends BaseRepository<Single> implements SearchableRepository<Single>{

    @Override
    public int add(List<String> columns, List<Object> values) {
        return addWithChild("SINGLES", List.of(), columns, values);
    }

    @Override
    public int update(int id, List<String> columns, List<Object> values) {
        return updateWithChild("SINGLES", List.of(), id, columns, values);
    }

    @Override
    protected Single mapRow(ResultSet rs) throws SQLException {
        Single s = new Single();
        s.setSong_id(rs.getInt("song_id"));
        s.setName(rs.getString("name"));
        s.setLength(rs.getInt("length"));
        s.setRelease_date(rs.getDate("release_date").toLocalDate());
        s.setStreams(rs.getInt("streams"));
        return s;
    }

    @Override
    protected String getTableName() {
        return "SINGLES JOIN SONGS ON SINGLES.song_id = SONGS.song_id";
    }

    @Override
    protected String getBaseTableName() {
        return "SONGS";
    }

    @Override
    protected String getIdColumnName() {
        return "song_id";
    }

    @Override
    public List<Single> searchByName(String name) {
        return searchByColumnName("name", name);
    }

    @Override
    public String getCategoryName() {
        return "Singles";
    }

    public int getIdByNameAndArtist(String songName, int artistId) {
        String sql = "SELECT S.song_id FROM SONGS S " +
                "JOIN SONG_ARTISTS SA ON S.song_id = SA.song_id " +
                "WHERE LOWER(S.name) = LOWER(?) AND SA.creator_id = ?";
        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql)) {
            stmt.setString(1, songName);
            stmt.setInt(2, artistId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error finding single by name and artist: " + e.getMessage());
        }
        return -1;
    }
}
