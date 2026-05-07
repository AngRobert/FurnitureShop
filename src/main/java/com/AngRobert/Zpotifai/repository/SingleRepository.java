package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.Single;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SingleRepository extends BaseRepository<Single> implements SearchableRepository<Single> {
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
}
