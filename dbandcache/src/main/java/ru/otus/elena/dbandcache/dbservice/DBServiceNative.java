
package ru.otus.elena.dbandcache.dbservice;

import ru.otus.elena.dbandcache.interfaces.DBService;
import ru.otus.elena.dbandcache.executors.SimpleExecutor;
import ru.otus.elena.dbandcache.executors.TExecutor;
import ru.otus.elena.dbandcache.executors.TransactionExecutor;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ru.otus.elena.dbandcache.dataset.base.DataSet;
import ru.otus.elena.cache.DBCache;

public class DBServiceNative implements DBService{
    private MysqlDataSource source;
    private final String host;
    private final String databaseName;
    private final String user;
    private final String password;
    public boolean useCache;
    public DBCache cache;
    private DBServiceHelper dbhelper;
    private Connection connection;
    
    public DBServiceNative(){
       
       this.host="localhost";
       this.databaseName="test";
       this.user="root";
       this.password="root";
       dbhelper=new DBServiceHelper(this);
    }
    public DBServiceNative(String host,String databaseName,String user,String password){
        this.host=host;
        this.databaseName=databaseName;
        this.user=user;
        this.password=password;
        dbhelper=new DBServiceHelper(this);
    }

    
    public Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            try {
                source = new MysqlDataSource();
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

    @Override
    public void createTable(Class<? extends DataSet> clazz) {
        try {
            if (dbhelper.tableExists(clazz)) {
                System.out.println("Table already exists");
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
        @Override
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


    @Override
    public void deleteTable(Class<? extends DataSet> clazz) {
        try {
            if (!dbhelper.tableExists(clazz)) {
                return;
            }
            String command = "DROP TABLE " + clazz.getSimpleName();
            SimpleExecutor exec = new SimpleExecutor(getConnection());
            exec.execUpdate(command);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
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
    public <T extends DataSet> int saveAll(T... data) {
        try {
            ArrayList<String> commands = dbhelper.getSaveCommand(data);
            if (commands != null && commands.size() > 0) {
                TransactionExecutor exec = new TransactionExecutor(getConnection());
                return exec.execUpdate(commands);
            }
            return 0;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return 0;
        }
    }

    @Override
    public <T extends DataSet> void save(T object) {
        saveAll(object);
    }


    @Override
    public <T extends DataSet> T loadById(long id, Class<T> clazz) {
        T data = null;
        if (useCache) {
            data = cache.get(id,clazz);
            if (data != null) {
                return data;
            }
        }
        try {
            String command = dbhelper.getLoadCommand(id, clazz);
            if (command == null) {
                return null;
            }
            TExecutor exec = new TExecutor(getConnection());
            data = exec.execQuery(command, result -> {
                ArrayList<T> list = (ArrayList<T>) dbhelper.createObjects(result, clazz, null);
                if (list.isEmpty()) {
                    return null;
                }
                T obj = list.get(0);
                return obj;
            });
            return data;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
        
    }
        
    @Override
    public <T extends DataSet> T loadByName(String name, Class<T> clazz) {
        return null;
    }

    @Override
    public <T extends DataSet> List<T> load(Class<T> clazz) {
        return null;
    }
    
    @Override
    public void setCache(DBCache cache) {
       this.useCache=true;
       this.cache=cache;
       dbhelper.setCache(cache);
    }

    @Override
    public void shutDown(){
        try {
            if (connection != null) {
                connection.close();            
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }


}
