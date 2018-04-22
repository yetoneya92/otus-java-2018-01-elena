package ru.otus.elena.dbservice.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultHandler {

   public void handle(ResultSet result) throws SQLException;
}
