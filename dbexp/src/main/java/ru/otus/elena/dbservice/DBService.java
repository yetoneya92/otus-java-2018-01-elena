
package ru.otus.elena.dbservice;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import ru.otus.elena.dataset.DataSet;

public class DBService {
    private final String host;
    private final String databaseName;
    private final String user;
    private final String password;
    private DBServiceHelper dbhelper;
    private Connection connection;
    public DBService(){
       this.host="localhost";
       this.databaseName="test";
       this.user="root";
       this.password="root";
       dbhelper=new DBServiceHelper(this);
    }
    public DBService(String host,String databaseName,String user,String password){
        this.host=host;
        this.databaseName=databaseName;
        this.user=user;
        this.password=password;
        dbhelper=new DBServiceHelper(this);
    }

    protected Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            try {
                MysqlDataSource source = new MysqlDataSource();
                source.setServerName(host);
                source.setDatabaseName(databaseName);
                source.setUser(user);
                source.setPassword(password);
                connection=source.getConnection();
                return connection;
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                return null;
            }
        }
    }

    public void createTable(Class<? extends DataSet> clazz) {
        try {
            if (dbhelper.tableExist(clazz)) {
                return;
            }
            String command = dbhelper.getTableCreateCommand(clazz);
            if (command != null) {
                SimpleExecutor exec = new SimpleExecutor(getConnection());
                exec.execUpdate(command);
            } else {
                System.out.println("невозможно создать таблицу");
                System.exit(0);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    public <T extends DataSet> int save(T... data){
        try {            
            ArrayList<String> commands = new ArrayList<>();
            for (T object : data) {
                String command = dbhelper.getSaveCommand(object);
                if (command != null) {
                    commands.add(command);
                } else {
                    return 0;
                }
            }
            if (commands.size() > 0) {
                TransactionExecutor exec = new TransactionExecutor(getConnection());
                return exec.execUpdate(commands);
            }
            return 0;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return 0;
        }
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        try {
            String command = dbhelper.getLoadCommand(id, clazz);
            if (command == null) {
                return null;
            }
            TExecutor exec = new TExecutor(getConnection());
            T data = (T)exec.execQuery(command, result -> {
                T obj = null;
                boolean isEmpty = true;
                s:
                try {
                    while (result.next()) {
                        isEmpty = false;
                        obj = (T)dbhelper.createObject(result,clazz);
                        break s;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if (isEmpty) {
                    return null;
                }
                return obj;
            });
            return data;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getTableNames() {
        try {
            ArrayList<String> tableNames = new ArrayList<>();
            try (ResultSet res = getConnection().getMetaData().getTables(databaseName, "", "%", null)) {
                while (res.next()) {
                    tableNames.add(res.getString("TABLE_NAME"));
                }
                return tableNames;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }


    public void deleteTable(Class<? extends DataSet> clazz) {
        try {
            if (!dbhelper.tableExist(clazz)) {
                return;
            }
            String command = "DROP TABLE " + clazz.getSimpleName();
            SimpleExecutor exec = new SimpleExecutor(getConnection());
            exec.execUpdate(command);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void deleteAllTables() {
        try {
            ArrayList<String> names = getTableNames();
            if (names.isEmpty()) {
                return;
            }
            ArrayList<String> commands = new ArrayList<>();
            for (String name : names) {
                commands.add("DROP TABLE " + name);
            }
            TransactionExecutor exec = new TransactionExecutor(getConnection());
            exec.execUpdate(commands);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void shutDown() throws SQLException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

}
