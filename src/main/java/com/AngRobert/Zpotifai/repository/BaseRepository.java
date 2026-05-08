package com.AngRobert.Zpotifai.repository;

import com.AngRobert.Zpotifai.util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T> {

    protected abstract T mapRow(ResultSet rs) throws SQLException;
    protected abstract String getTableName();
    protected abstract String getIdColumnName();

    protected String getNameColumnName() {
        return "name";
    }

    protected String getBaseTableName() {
        return getTableName();
    }

    public int getIdByName(String name) {
        String sql = "SELECT " + getIdColumnName() + " FROM " + getBaseTableName() + " WHERE LOWER(" + getNameColumnName() + ") = LOWER(?)";
        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error finding ID by name: " + e.getMessage());
        }
        return -1;
    }

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
        String sql = "SELECT * FROM " + getTableName() + " WHERE LOWER(" + columnName + ") LIKE LOWER(?)";
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

    public int add(String tableName, List<String> columns, List<Object> values) {
        if (columns.size() != values.size()) {
            throw new IllegalArgumentException("The number of values inputted must match the number of columns!");
        }

        StringBuilder sql = new StringBuilder("INSERT INTO ")
                .append(tableName)
                .append(" (")
                .append(String.join(", ", columns))
                .append(") VALUES (");

        for (int i = 0; i < values.size(); i++) {
            sql.append("?");
            if (i < values.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                // returneaza id-ul generat pentru a fi folosit in subclase
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error inserting into " + tableName + ": " + e.getMessage());
        }
        return -1;
    }

    public int add(List<String> columns, List<Object> values) {
        return add(getBaseTableName(), columns, values);
    }

    protected int addWithChild(String childTableName, List<String> childOnlyColumns, List<String> columns, List<Object> values) {
        List<String> parentCols = new ArrayList<>();
        List<Object> parentVals = new ArrayList<>();
        List<String> childCols = new ArrayList<>();
        List<Object> childVals = new ArrayList<>();

        for (int i = 0; i < columns.size(); i++) {
            String col = columns.get(i);
            boolean isChild = false;
            for (String childCol : childOnlyColumns) {
                if (childCol.equalsIgnoreCase(col)) {
                    isChild = true;
                    break;
                }
            }

            if (isChild) {
                childCols.add(col);
                childVals.add(values.get(i));
            } else {
                parentCols.add(col);
                parentVals.add(values.get(i));
            }
        }

        int id = add(getBaseTableName(), parentCols, parentVals);
        if (id != -1) {
            childCols.add(getIdColumnName());
            childVals.add(id);
            add(childTableName, childCols, childVals);
        }
        return id;
    }

    public void update(int id, List<String> columns, List<Object> values) {
        if (columns.size() != values.size()) {
            throw new IllegalArgumentException("The number of values inputted must match the number of columns!");
        }

        StringBuilder sql = new StringBuilder("UPDATE ")
                .append(getBaseTableName())
                .append(" SET ");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i)).append(" = ?");
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE ").append(getIdColumnName()).append(" = ?");

        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            stmt.setInt(values.size() + 1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating: " + e.getMessage());
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM " + getBaseTableName() + " WHERE " + getIdColumnName() + " = ?";
        try (PreparedStatement stmt = DBConnection.get().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
