
package ru.otus.elena.dbservice.execution;

import ru.otus.elena.dbservice.execution.SaveExecution;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dbservice.ServiceConnection;
import ru.otus.elena.dbservice.dbservice.ServiceLoad;
import ru.otus.elena.dbservice.executors.TExecutor;

@Service
public class LoadExecution {

    private boolean parametrize;
    private final ServiceConnection serviceConnection;
    private final ServiceLoad serviceLoad;

    @Autowired
    public LoadExecution(ServiceConnection serviceConnection, ServiceLoad serviceLoad) {
        this.serviceConnection = serviceConnection;
        this.serviceLoad=serviceLoad;
    }

    public <T extends DataSet> ArrayList<T> load(String command, Class<T> clazz) {
        try {
            TExecutor exec = new TExecutor(serviceConnection.getConnection());
            ArrayList<T> data = exec.execQuery(command, result -> {
                ArrayList<T> list = (ArrayList<T>) createObjects(result, clazz, null);
                if (list.isEmpty()) {
                    return null;
                }
                result.close();
                return list;
            });
            return data;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    public <T, R extends DataSet> ArrayList<T> createObjects(ResultSet result, Class<T> clazz, DataSet init) {
        try {
            ArrayList<T> objects = new ArrayList<>();
            while (result.next()) {
                T object = clazz.newInstance();
                Field[] fields = clazz.getFields();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);

                }
                long id = result.getLong(1);
                fields[fields.length - 1].setLong(object, id);

                for (int i = 0; i < fields.length - 1; i++) {
                    if (fields[i].getGenericType() instanceof ParameterizedType) {
                        parametrize = true;
                        Set<R> coll = new HashSet<>();
                        ArrayList<R> list = (ArrayList<R>) findElements(id, fields[i], (DataSet) object);
                        coll.addAll(list);
                        fields[i].set(object, coll);
                    }
                }
                ResultSetMetaData metadata = result.getMetaData();
                int columns = metadata.getColumnCount();
                for (int i = 2; i <= columns; i++) {
                    String columnName = metadata.getColumnName(i);
                    if (columnName.contains("_id")) {
                        long _id = result.getLong(columnName);
                        R element = null;
                        t:
                        for (int j = 0; j < fields.length - 1; j++) {
                            if (columnName.contains(fields[j].getType().getSimpleName().toLowerCase())) {
                                if (parametrize) {
                                    fields[j].set(object, init);
                                    break t;
                                } else {
                                    ArrayList<String>messages=new ArrayList<>();
                                    String command=serviceLoad.getLoadCommand(id, (Class<R>) fields[j].getType(), messages);
                                    element = (R) load(command,(Class<R>) fields[j].getType()).get(0);
                                    if (element == null) {
                                        return null;
                                    }
                                    fields[j].set(object, element);
                                    break t;
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < fields.length - 1; j++) {
                            if (columnName.contains(fields[j].getName().toLowerCase())) {
                                if (metadata.getColumnTypeName(i).equals("INT") && fields[j].getType().equals(int.class)) {
                                    fields[j].set(object, result.getInt(i));
                                } else if (metadata.getColumnTypeName(i).equals("CHAR") && fields[j].getType().equals(String.class)) {
                                    fields[j].set(object, result.getString(i));
                                }
                            }
                        }
                    }
                }
                objects.add(object);
            }
            return objects;
        } catch (SQLException | InstantiationException
                | IllegalAccessException | IllegalArgumentException sqle) {
            sqle.printStackTrace();
            return null;
        } finally {
            try {
                result.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
        public <T, R extends DataSet> ArrayList<R> findElements(long id, Field field, T object) throws SQLException {
        ParameterizedType pType = (ParameterizedType) field.getGenericType();
        Type[] fieldArgsTypes = pType.getActualTypeArguments();
        for (Type type : fieldArgsTypes) {
            Class pclazz = (Class) type;
            String tableName = pclazz.getSimpleName().toLowerCase();
            String objectName = object.getClass().getSimpleName().toLowerCase();
            String command = "SELECT*FROM " + tableName + " WHERE " + tableName + "_" + objectName + "_id LIKE " + id;
            TExecutor exec = new TExecutor(serviceConnection.getConnection());
            ArrayList<R> elements = (ArrayList<R>) exec.execQuery(command, result -> {
                return createObjects(result, pclazz, (DataSet) object);
            });
            setParametrize(false);
            return elements;
        }
        return null;
    }    

    public void setParametrize(boolean parametrize) {
        this.parametrize = parametrize;
    }
}
