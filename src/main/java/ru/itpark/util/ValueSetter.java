package ru.itpark.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ValueSetter {
    void setValues(PreparedStatement stmt) throws SQLException;
}
