package com.home.calories.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class DbUtil {

    private DbUtil() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Enum<T>> T getEnum(ResultSet rs, String column, Class<T> clazz) throws SQLException {
        String result = rs.getString(column);
        return result == null ? null : Enum.valueOf(clazz, result);
    }

    public static Long getLong(ResultSet rs, String column) throws SQLException {
        long result = rs.getLong(column);
        return rs.wasNull() ? null : result;
    }

    public static Integer getInteger(ResultSet rs, String column) throws SQLException {
        int result = rs.getInt(column);
        return rs.wasNull() ? null : result;
    }

    public static Instant getInstant(ResultSet rs, String column) throws SQLException {
        var result = rs.getTimestamp(column);
        return rs.wasNull() ? null : result.toInstant();
    }

}
