
package ru.otus.elena.dbservice.executors;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TransactionExecutor {
        Connection connection;

    public TransactionExecutor(Connection connection) {
        this.connection = connection;
    }

    public int execUpdate(ArrayList<String> command) throws SQLException {
        try {
            int counter = 0;
            connection.setAutoCommit(false);
            try (Statement stmt = connection.createStatement()) {
                for (String com : command) {
                    if (command != null) {
                        boolean b = stmt.execute(com);
                        if (!b) {
                            counter += stmt.getUpdateCount();
                        }
                    }
                }
                connection.commit();
                return counter;

            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            connection.rollback();
            return 0;
        }
    }
}
