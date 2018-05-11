package ru.otus.elena.dbandcache.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultHandler {

   public void handle(ResultSet result) throws SQLException;
}
