package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.AlbumTrack;
import com.AngRobert.Zpotifai.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class AlbumTrackRepository extends BaseRepository<AlbumTrack> implements SearchableRepository<AlbumTrack>
{
    @Override
    public int add(List<String> columns, List<Object> values) {
        return addWithChild("ALBUM_TRACKS", List.of("track_number", "album_id"), columns, values);
    }

    @Override
    public int update(int id, List<String> columns, List<Object> values) {
        return updateWithChild("ALBUM_TRACKS", List.of("track_number", "album_id"), id, columns, values);
    }

    @Override
    protected AlbumTrack mapRow(ResultSet rs) throws SQLException {
        AlbumTrack a = new AlbumTrack();
        a.setSong_id(rs.getInt("song_id"));
        a.setName(rs.getString("name"));
        a.setTrack_number(rs.getInt("track_number"));
        a.setLength(rs.getInt("length"));
        a.setRelease_date(rs.getDate("release_date").toLocalDate());
        a.setStreams(rs.getInt("streams"));
        return a;
    }

    @Override
    protected String getTableName() {
        return "ALBUM_TRACKS JOIN SONGS ON ALBUM_TRACKS.song_id = SONGS.song_id";
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
    public List<AlbumTrack> searchByName(String name) {
        return searchByColumnName("name", name);
    }

    @Override
    public String getSearchDetails(int id) {
        String baseDetails = super.getSearchDetails(id, List.of("name", "length", "streams", "release_date", "track_number"));
        StringBuilder details = new StringBuilder(baseDetails);

        List<String> album = getRelatedNames(
                "SELECT A.name FROM ALBUMS A JOIN ALBUM_TRACKS AT ON A.album_id = AT.album_id WHERE AT.song_id = ?",
                id
        );
        details.append("Album: ").append(album.isEmpty() ? "None" : album.get(0)).append("\n");

        return details.toString();
    }

    @Override
    public String getCategoryName() {
        return "Album Tracks";
    }

    public int getIdByNameAndAlbum(String songName, int albumId) {
        String sql = "SELECT S.song_id FROM SONGS S " +
                "JOIN ALBUM_TRACKS AT ON S.song_id = AT.song_id " +
                "WHERE LOWER(S.name) = LOWER(?) AND AT.album_id = ?";
        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql)) {
            stmt.setString(1, songName);
            stmt.setInt(2, albumId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error finding album track by name and album: " + e.getMessage());
        }
        return -1;
    }

    public List<Integer> getSongIdsByAlbumId(int albumId) {
        String sql = "SELECT song_id FROM ALBUM_TRACKS WHERE album_id = ?";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql)) {
            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) ids.add(rs.getInt(1));
        } catch (SQLException e) {
            System.out.println("Error fetching song IDs for album: " + e.getMessage());
        }
        return ids;
    }
}
