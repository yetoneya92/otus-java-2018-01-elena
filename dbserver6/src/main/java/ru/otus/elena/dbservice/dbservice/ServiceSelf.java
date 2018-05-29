
package ru.otus.elena.dbservice.dbservice;

import ru.otus.elena.dbservice.executors.SimpleExecutor;
import ru.otus.elena.dbservice.executors.TExecutor;
import ru.otus.elena.dbservice.executors.TransactionExecutor;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dataset.tables.Table;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.cache.Cache;

@Component

public class ServiceSelf implements Service{
    @Autowired
    private MysqlDataSource source; 
    private  String host;
    private  String databaseName;
    private  String user;
    private  String password;
    public boolean useCache;
    public Cache cache;
    
    private ServiceHelper dbhelper;
    private Connection connection;
      
    public ServiceSelf(){       
      // this.host="localhost";
       //this.databaseName="db_example";
      // this.user="me";
      // this.password="me";
       dbhelper=new ServiceHelper(this);
    }
    
    public ServiceSelf(String host,String databaseName,String user,String password){
        this.host=host;
        this.databaseName=databaseName;
        this.user=user;
        this.password=password;
        dbhelper=new ServiceHelper(this);
    }

    
    public Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            try {
                //source = new MysqlDataSource();
                //source.setServerName(host);
                //source.setDatabaseName(databaseName);
                //source.setUser(user);
                //source.setPassword(password);
                connection=source.getConnection();
                return connection;
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public <T extends DataSet> boolean createTable(String tableName) {
        try {
            Table table = Table.valueOf(tableName);
            Class<T> clazz = table.getClazz();
            boolean isCreated = createTable(clazz);
            return isCreated;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean createTable(Class<? extends DataSet> clazz) {
        
            try {
                if (dbhelper.tableExists(clazz)) {
                    System.out.println("Table already exists");
                    return false;
                }
                String command = dbhelper.getTableCreateCommand(clazz);
                if (command != null) {
                    SimpleExecutor exec = new SimpleExecutor(getConnection());
                    exec.execUpdate(command);
                    return true;
                } else {
                    System.out.println("невозможно создать таблицу");
                    System.exit(0);
                    return false;
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                return false;
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
    public<T extends DataSet> boolean deleteTable(String tableName){
        try{Table table=Table.valueOf(tableName);       
            Class<T>clazz=table.getClazz();
            boolean isDelete=deleteTable(clazz);
            return isDelete;}
        catch(Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteTable(Class<? extends DataSet> clazz) {
        try {
            if (!dbhelper.tableExists(clazz)) {
                return false;
            }
            String command = "DROP TABLE " + clazz.getSimpleName();
            SimpleExecutor exec = new SimpleExecutor(getConnection());
            exec.execUpdate(command);
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAllTables() {
        try {
            ArrayList<String> names = getTableNames();
            if (names.isEmpty()) {
                return true;
            }
            ArrayList<String> commands = new ArrayList<>();
            for (String name : names) {
                commands.add("DROP TABLE " + name);
            }
            TransactionExecutor exec = new TransactionExecutor(getConnection());
            exec.execUpdate(commands);
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    public <T extends DataSet> int saveAll(T... data) {
        for (T object : data) {
            if (!dbhelper.tableExists(object.getClass())) {
                createTable(object.getClass());
            }
        }
        try {
            ArrayList<T> objects = new ArrayList<>(Arrays.asList(data));
            ArrayList<String> commands = dbhelper.getSaveCommand(objects);
            if (useCache) {
                objects.forEach(s -> cache.put(s));
            }
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
    public <T extends DataSet> int save(T object) {
        return saveAll(object);
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
    public void setCache(Cache cache) {
       this.useCache=true;
       this.cache=cache;
       dbhelper.setCache(cache);
    }

    @Override
    public void shutDown() {
        try {
            if (connection != null) {
                connection.close();

            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
