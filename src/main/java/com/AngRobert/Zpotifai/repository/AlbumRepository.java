package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.Album;

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
}
