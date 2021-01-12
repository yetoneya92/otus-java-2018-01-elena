package ru.otus.elena.dbservice.executors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import ru.otus.elena.dbservice.interfaces.ExecuteHandler;

public class PreparedExecutor {

    Connection connection;

    public PreparedExecutor(Connection connection) {
        this.connection = connection;
    }

    public void execUpdate(String update, ExecuteHandler prepare) {
        try {
            PreparedStatement stmt = connection.prepareStatement(update,Statement.RETURN_GENERATED_KEYS);
            prepare.accept(stmt);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
