
package ru.otus.elena.dbservice.executors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import ru.otus.elena.dbservice.dataset.base.DataSet;

public class TransactionExecutor {
        Connection connection;

    public TransactionExecutor(Connection connection) {
        this.connection = connection;
    }
    
    public <T extends DataSet> int execUpdate(Map<T, String> commands) {
        try {
            int[] counter = new int[1];
            connection.setAutoCommit(false);
            try (Statement stmt = connection.createStatement()) {
                commands.forEach((k, v) -> {
                    try {
                        if (v != null) {
                            boolean b = stmt.execute(v, Statement.RETURN_GENERATED_KEYS);
                            ResultSet keys = stmt.getGeneratedKeys();
                            while (keys.next()) {
                                k.setId(keys.getLong(1));
                                break;
                            }
                            keys.close();
                            if (!b) {
                                counter[0] += stmt.getUpdateCount();
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
                connection.commit();
                return counter[0];
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
        }
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
