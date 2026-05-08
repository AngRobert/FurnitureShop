package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.Album;
import com.AngRobert.Zpotifai.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AlbumRepository extends BaseRepository<Album> implements SearchableRepository<Album> {

    @Override
    protected Album mapRow(ResultSet rs) throws SQLException {
        Album a = new Album();
        a.setAlbum_id(rs.getInt("album_id"));
        a.setName(rs.getString("name"));
        a.setRelease_date(rs.getDate("release_date").toLocalDate());
        return a;
    }

    @Override
    protected String getTableName() {
        return "Albums";
    }

    @Override
    public String getCategoryName() {
        return "Albums";
    }

    @Override
    protected String getIdColumnName() {
        return "album_id";
    }

    @Override
    public List<Album> searchByName(String name) {
        return this.searchByColumnName("name", name);
    }

    public int getIdByNameAndArtist(String albumName, int artistId) {
        String sql = "SELECT A.album_id FROM ALBUMS A " +
                "JOIN ALBUM_ARTISTS AA ON A.album_id = AA.album_id " +
                "WHERE LOWER(A.name) = LOWER(?) AND AA.creator_id = ?";
        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql)) {
            stmt.setString(1, albumName);
            stmt.setInt(2, artistId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error finding album by name and artist: " + e.getMessage());
        }
        return -1;
    }
}
