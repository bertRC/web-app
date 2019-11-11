package ru.itpark.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementExecutor<T> {
    T execute(PreparedStatement stmt) throws SQLException;
}
