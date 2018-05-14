
package ru.otus.elena.dbandcache.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ExecuteHandler {

  public void accept(PreparedStatement statement) throws SQLException;
}