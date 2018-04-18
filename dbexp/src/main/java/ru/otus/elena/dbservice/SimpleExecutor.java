
package ru.otus.elena.dbservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SimpleExecutor {

    Connection connection;

    public SimpleExecutor(Connection connection) {
        this.connection = connection;
    }

    public int execUpdate(String command) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(command);
            return stmt.getUpdateCount();

        }
    }
}
