
package ru.otus.elena.dbservice.dbservice;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import ru.otus.elena.cache.DBCache;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.executors.SimpleExecutor;
import ru.otus.elena.dbservice.executors.TExecutor;
import ru.otus.elena.dbservice.executors.TransactionExecutor;

public class ServiceExecution {

    private static volatile ServiceExecution serviceExecution = null;
    private String databaseName;
    private MysqlDataSource source;
    private volatile Connection connection;
    private DBCache cache;

    private ServiceExecution() {             
        source = new MysqlDataSource();
        source.setServerName("localhost");
        source.setDatabaseName("db_example");
        source.setUser("me");
        source.setPassword("me");
        databaseName="db_example";        
    }

    private ServiceExecution(String host, String databaseName, String user, String password) {        
        source = new MysqlDataSource();
        source.setServerName(host);
        source.setDatabaseName(databaseName);
        source.setUser(user);
        source.setPassword(password);
        this.databaseName = databaseName;
    }

    public static ServiceExecution getServiceExecution() {
        if (serviceExecution == null) {
            serviceExecution = new ServiceExecution();
        }
        return serviceExecution;
    }

    public static ServiceExecution getServiceExecution(String host, String databaseName, String user, String password) {
        if (serviceExecution == null) {
            serviceExecution = new ServiceExecution(host, databaseName, user, password);
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
    
    public void setCache(DBCache cache) {
        this.cache = cache;
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
                ArrayList<T> list = (ArrayList<T>) ServiceCreator.getServiceCreator().createObjects(result, clazz, null);
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
    
    <T extends DataSet> long findId(T object, String tname, boolean saveobject, ServiceCommand serviceCommand) {
        try {
            String command = "SELECT*FROM " + tname;
            TExecutor exec = new TExecutor(getConnection());
            long _id = exec.execQuery(command, result -> {
                long id = 0;
                boolean isEmpty = true;

                m:
                while (result.next()) {
                    isEmpty = false;
                    if (serviceCommand.getParametrize()) {
                        break m;
                    }
                    s:
                    for (Field field : object.getClass().getDeclaredFields()) {
                        try {
                            if (field.getType().equals(int.class) || field.getType().equals(String.class)) {
                                if (result.getObject(tname + "_" + field.getName().toLowerCase(), field.getType()).equals(field.get(object))) {
                                    continue s;
                                } else {
                                    continue m;
                                }
                            } else {

                                System.out.println(field.get(object).getClass().getSimpleName().toLowerCase());
                                long findInTables = findId((DataSet) field.get(object), field.get(object).getClass().getSimpleName().toLowerCase(), true, serviceCommand);//???
                                long current = result.getObject(tname + "_" + field.getName().toLowerCase() + "_id", long.class);
                                if (findInTables == current) {
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
                        ServiceSelf.getService().save((DataSet) object);
                        if (isEmpty) {
                            id = 1;
                              
                        } else {
                            id = result.getLong(1) + 1;                            
                        }
                        serviceCommand.setLast(id);
                    } else {
                        System.out.println(object.toString() + " exists");
                    }
                }
                else {
                    if (isEmpty) {
                        serviceCommand.setLast(0);
                    } else {
                        serviceCommand.setLast(result.getLong(1));
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
    
    <T, R extends DataSet> ArrayList<R> findElements(long id, Field field, T object, ServiceCreator creator) throws SQLException {
        ParameterizedType pType = (ParameterizedType) field.getGenericType();
        //System.out.println(pType);
        Type[] fieldArgsTypes = pType.getActualTypeArguments();
        for (Type type : fieldArgsTypes) {
            //System.out.println(type);
            Class pclazz = (Class) type;
            String tableName = pclazz.getSimpleName().toLowerCase();
            String objectName = object.getClass().getSimpleName().toLowerCase();
            String command = "SELECT*FROM " + tableName + " WHERE " + tableName + "_" + objectName + "_id LIKE " + id;
            TExecutor exec = new TExecutor(getConnection());
            ArrayList<R> elements = (ArrayList<R>) exec.execQuery(command, result -> {
                return ServiceCreator.getServiceCreator().createObjects(result, pclazz, (DataSet) object);
            });
            creator.setParametrize(false);
            return elements;
        }
        return null;
    }
    public<T extends DataSet>void removeDublicate(ArrayList<T>objects){
        Iterator<T> iterator = objects.iterator();
        s:
        while (iterator.hasNext()) {
            T object = iterator.next();
            if (object.getId() == 0) {
                continue s;
            }
            if (cache.get(object.getId(), object.getClass()) != null) {
                objects.remove(object);
                
            }
            
        }
    }


        public void shutDown(){
            serviceExecution=null;
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
