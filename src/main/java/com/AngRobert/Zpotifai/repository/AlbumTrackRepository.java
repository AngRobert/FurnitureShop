package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.AlbumTrack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AlbumTrackRepository extends BaseRepository<AlbumTrack> implements SearchableRepository<AlbumTrack>
{
    @Override
    public int add(List<String> columns, List<Object> values) {
        return addWithChild("ALBUM_TRACKS", List.of("track_number", "album_id"), columns, values);
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
    public String getCategoryName() {
        return "Album Tracks";
    }
}
