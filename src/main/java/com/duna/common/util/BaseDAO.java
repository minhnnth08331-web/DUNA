package com.duna.common.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lop co so cho tat ca DAO: dung PreparedStatement + try-with-resources an toan.
 */
public abstract class BaseDAO {

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    protected <T> List<T> queryList(String sql, RowMapper<T> mapper, Object... params) {
        List<T> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Loi truy van SQL: " + e.getMessage(), e);
        }
        return list;
    }

    protected <T> T queryOne(String sql, RowMapper<T> mapper, Object... params) {
        List<T> items = queryList(sql, mapper, params);
        return items.isEmpty() ? null : items.get(0);
    }

    protected List<Map<String, Object>> queryRows(String sql, Object... params) {
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= cols; i++) {
                        String key = meta.getColumnLabel(i);
                        if (key == null || key.isEmpty()) {
                            key = "col_" + i;
                        }
                        row.put(key, rs.getObject(i));
                    }
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Loi truy van SQL: " + e.getMessage(), e);
        }
        return rows;
    }

    protected Map<String, Object> queryFirstRow(String sql, Object... params) {
        List<Map<String, Object>> rows = queryRows(sql, params);
        return rows.isEmpty() ? null : rows.get(0);
    }

    protected int queryInt(String sql, Object... params) {
        Integer v = queryOne(sql, rs -> rs.getInt(1), params);
        return v == null ? 0 : v;
    }

    protected double queryDouble(String sql, Object... params) {
        Double v = queryOne(sql, rs -> rs.getDouble(1), params);
        return v == null ? 0.0 : v;
    }

    protected int executeUpdate(String sql, Object... params) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindParams(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Loi cap nhat SQL: " + e.getMessage(), e);
        }
    }

    protected long executeInsertGetId(String sql, Object... params) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindParams(ps, params);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
            return 0L;
        } catch (SQLException e) {
            throw new RuntimeException("Loi them moi SQL: " + e.getMessage(), e);
        }
    }

    protected static void bindParams(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    protected static String likePattern(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return "%%";
        }
        return "%" + keyword.trim() + "%";
    }
}
