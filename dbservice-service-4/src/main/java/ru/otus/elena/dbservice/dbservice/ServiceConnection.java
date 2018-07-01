
package ru.otus.elena.dbservice.dbservice;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceConnection {
    @Autowired
    private MysqlDataSource source;
    private Connection connection;
    
    public ServiceConnection(){        
    }
    
    public Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            try {
                connection = source.getConnection();
                return connection;
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                return null;
            }
        }
    }
    
    
        public void shutDown(){
        try {
            if (connection != null) {                
                connection.close(); 
                connection=null;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
