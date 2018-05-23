
package ru.otus.elena.dbservice.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface TResultHandler<T> {

    T handle(ResultSet resultSet) throws SQLException;
}
