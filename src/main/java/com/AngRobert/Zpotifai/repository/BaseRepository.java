package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.model.Searchable;
import com.AngRobert.Zpotifai.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T> {

    protected abstract T mapRow(ResultSet rs) throws SQLException;
    protected abstract String getTableName();
    protected abstract String getIdColumnName();

    public T findById(int id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<T> searchByColumnName(String columnName, String value) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + columnName + " LIKE ?";
        List<T> rez = new ArrayList<>();
        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql)) {
            stmt.setString(1, "%" + value + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rez.add(mapRow(rs));
            }
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return rez;
    }

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
