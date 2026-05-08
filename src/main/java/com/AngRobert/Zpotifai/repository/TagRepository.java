package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TagRepository extends BaseRepository<Tag> {

    @Override
    protected Tag mapRow(ResultSet rs) throws SQLException {
        Tag t = new Tag();
        t.setTag_id(rs.getInt("tag_id"));
        t.setDescription(rs.getString("description"));
        return t;
    }

    @Override
    protected String getTableName() {
        return "TAGS";
    }

    @Override
    protected String getIdColumnName() {
        return "tag_id";
    }

    @Override
    protected String getNameColumnName() {
        return "description";
    }
}
