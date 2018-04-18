
package ru.otus.elena.dbservice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import ru.otus.elena.dbservice.interfaces.TResultHandler;

public class TExecutor {

    private final Connection connection;

    public TExecutor(Connection connection) {
        
        this.connection = connection;
    }

    public <T> T execQuery(String query, TResultHandler<T> handler) throws SQLException {
        try(Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        }
    }

}
