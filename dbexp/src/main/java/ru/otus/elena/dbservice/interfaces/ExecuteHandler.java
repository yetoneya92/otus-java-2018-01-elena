
package ru.otus.elena.dbservice.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ExecuteHandler {

  public void accept(PreparedStatement statement) throws SQLException;
}