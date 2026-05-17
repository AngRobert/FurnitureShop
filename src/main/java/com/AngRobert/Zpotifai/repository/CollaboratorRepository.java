package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.Collaborator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CollaboratorRepository extends BaseRepository<Collaborator> implements SearchableRepository<Collaborator> {

    @Override
    protected Collaborator mapRow(ResultSet rs) throws SQLException {
        Collaborator c = new Collaborator();
        c.setCollaborator_id(rs.getInt("collaborator_id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        return c;
    }

    @Override
    protected String getTableName() {
        return "COLLABORATORS";
    }

    @Override
    protected String getIdColumnName() {
        return "collaborator_id";
    }

    @Override
    public List<Collaborator> searchByName(String name) {
        return this.searchByColumnName("name", name);
    }

    @Override
    public String getSearchDetails(int id) {
        String baseDetails = super.getSearchDetails(id, List.of("name", "description"));
        StringBuilder details = new StringBuilder(baseDetails);

        List<String> songs = getRelatedNames(
                "SELECT S.name FROM SONGS S JOIN SONG_COLLABORATIONS SC ON S.song_id = SC.song_id WHERE SC.collaborator_id = ?",
                id
        );
        details.append("Songs: ").append(songs.isEmpty() ? "None" : String.join(", ", songs)).append("\n");

        return details.toString();
    }

    @Override
    public String getCategoryName() {
        return "Collaborators";
    }
}
