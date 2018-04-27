package ru.otus.elena.dbenchance.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultHandler {

   public void handle(ResultSet result) throws SQLException;
}
