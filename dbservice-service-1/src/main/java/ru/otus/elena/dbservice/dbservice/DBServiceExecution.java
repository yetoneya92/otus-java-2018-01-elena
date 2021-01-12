
package ru.otus.elena.dbservice.dbservice;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.executors.SimpleExecutor;
import ru.otus.elena.dbservice.executors.TExecutor;
import ru.otus.elena.dbservice.executors.TransactionExecutor;

public class DBServiceExecution {
    private static DBServiceExecution serviceExecution=null;
    private final String databaseName;    
    private final MysqlDataSource source; 
    private Connection connection;    

    private DBServiceExecution() {             
        source = new MysqlDataSource();
        source.setServerName("localhost");
        source.setDatabaseName("db_example");
        source.setUser("me");
        source.setPassword("me");
        databaseName="db_example";        
    }

    private DBServiceExecution(String host, String databaseName, String user, String password) {        
        source = new MysqlDataSource();
        source.setServerName(host);
        source.setDatabaseName(databaseName);
        source.setUser(user);
        source.setPassword(password);
        this.databaseName = databaseName;
    }

    public static DBServiceExecution getServiceExecution() {
        if (serviceExecution == null) {
            serviceExecution = new DBServiceExecution();
        }
        return serviceExecution;
    }

    public static DBServiceExecution getServiceExecution(String host, String databaseName, String user, String password) {
        if (serviceExecution == null) {
            serviceExecution = new DBServiceExecution(host, databaseName, user, password);
        }
        return serviceExecution;
    }
    
    private Connection getConnection() {
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

    boolean tableExists(Class<?> clazz) {
        ArrayList<String> tableNames = getTableNames();
        if (tableNames.contains(clazz.getSimpleName().toLowerCase())) {
            return true;
        }
        return false;
    }    
    
   public boolean createTable(String command) {        
            try {
                if (command != null) {
                    SimpleExecutor exec = new SimpleExecutor(getConnection());
                    int update=exec.execUpdate(command);
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                return false;
        }
    }

    public boolean deleteTable(String command) {
        try {
            SimpleExecutor exec = new SimpleExecutor(getConnection());
            int update = exec.execUpdate(command);
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }
    public boolean deleteAllTables(ArrayList<String> commands) {
        try {
            TransactionExecutor exec = new TransactionExecutor(getConnection());
            exec.execUpdate(commands);
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    public int saveAll(ArrayList<String> commands) {
        try {
            TransactionExecutor exec = new TransactionExecutor(getConnection());
            return exec.execUpdate(commands);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return 0;
        }

    }
    public <T extends DataSet> T loadById(String command, Class<T> clazz) {
        try {
            T data = null;
            TExecutor exec = new TExecutor(getConnection());
            data = exec.execQuery(command, result -> {
                ArrayList<T> list = (ArrayList<T>) DBServiceCreator.getServiceCreator().createObjects(result, clazz, null);
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

    public <T extends DataSet> T loadByName(String command, Class<T> clazz) {
        return null;
    }

    public <T extends DataSet> long findLast(String tableName) {
        try {
            String command = "SELECT*FROM " + tableName;
            TExecutor exec = new TExecutor(getConnection());
            long _id = exec.execQuery(command, result -> {
                long id = 0;
                while (result.next()) {
                    result.last();
                    id = result.getLong(1);
                }
                return id;
            });
            return _id;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        }
    }

    public <T extends DataSet> long findInTables(T object, String tableName, boolean saveobject) {
        try {
            String command = "SELECT*FROM " + tableName;
            TExecutor exec = new TExecutor(getConnection());
            long _id = exec.execQuery(command, result -> {
                long id = 0;
                boolean isEmpty = true;
                m:
                while (result.next()) {
                    isEmpty = false;
                    s:
                    for (Field field : object.getClass().getDeclaredFields()) {
                        try {
                            if (field.getType().equals(int.class) || field.getType().equals(String.class)) {
                                if (result.getObject(tableName + "_" + field.getName().toLowerCase(), field.getType()).equals(field.get(object))) {
                                    continue s;
                                } else {
                                    continue m;
                                }
                            } else if (field.getGenericType() instanceof ParameterizedType) {
                                continue s;
                            } else {
                                long find = findInTables((DataSet) field.get(object), field.get(object).getClass().getSimpleName().toLowerCase(), true);//from tables
                                long current = result.getObject(tableName + "_" + field.getName().toLowerCase() + "_id", long.class);//from result
                                if (find == current) {
                                    continue s;
                                } else {
                                    continue m;
                                }
                            }
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            ex.printStackTrace();
                            id = -1;
                            break m;
                        }
                    }
                    id = result.getLong(1);
                    
                    break m;
                }
                result.last();
                if (saveobject) {
                    if (id == 0) {
                        DBService.getService().save((DataSet) object);
                        if (isEmpty) {
                            id = 1;//id of saved object                              
                        } else {
                            id = result.getLong(1) + 1; //id of saved object                          
                        }
                    } else {
                        System.out.println(object.toString() + " exists");//id!=0
                    }
                } else {
                    if (id != 0) {
                        id = 0;//exists
                    } else {
                        if (isEmpty) {
                            id = 1;
                        } else {
                            id = result.getLong(1) + 1;//id for save
                        }
                    }
                }
                return id;
            });
            return _id;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        }
    }
    
    <T, R extends DataSet> ArrayList<R> findElements(long id, Field field, T object, DBServiceCreator creator) throws SQLException {
        ParameterizedType pType = (ParameterizedType) field.getGenericType();
        Type[] fieldArgsTypes = pType.getActualTypeArguments();
        for (Type type : fieldArgsTypes) {
            Class pclazz = (Class) type;
            String tableName = pclazz.getSimpleName().toLowerCase();
            String objectName = object.getClass().getSimpleName().toLowerCase();
            String command = "SELECT*FROM " + tableName + " WHERE " + tableName + "_" + objectName + "_id LIKE " + id;
            TExecutor exec = new TExecutor(getConnection());
            ArrayList<R> elements = (ArrayList<R>) exec.execQuery(command, result -> {
                return DBServiceCreator.getServiceCreator().createObjects(result, pclazz, (DataSet) object);
            });
            creator.setParametrize(false);
            return elements;
        }
        return null;
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
