
package ru.otus.elena.dbservice;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import ru.otus.elena.dataset.DataSet;

public class DBServiceHelper {
    private DBService service;
    
    public DBServiceHelper(DBService service){
        this.service=service;
    }
    
        protected String getTableCreateCommand(Class<? extends DataSet>clazz) {
        String tableName=clazz.getSimpleName().toLowerCase();
        StringBuilder builder=new StringBuilder
        ("CREATE TABLE "+tableName+"("+tableName+"_id BIGINT(20) NOT NULL AUTO_INCREMENT,");        
        for(Field field:clazz.getDeclaredFields()){
            field.setAccessible(true);
            if (field.getType().getSimpleName().equals("int")) {
                builder.append(tableName).append("_").append(field.getName().toLowerCase()).append(" INT(20),");
            } else if (field.getType().getSimpleName().equals("String")) {
                builder.append(tableName).append("_").append(field.getName().toLowerCase()).append(" CHAR(25),");
            } else if (field.getType().getCanonicalName().contains("java")) {
                return null;
            } else {
                builder.append(tableName).append("_").append(field.getType().getSimpleName().toLowerCase() + "_id").append(" INT(20),");
            }
        }
        builder.append("PRIMARY KEY("+tableName+"_id))");
            System.out.println(builder.toString());
        return builder.toString();
    }

     <T extends DataSet> String getSaveCommand(T object){   
         try {
             ArrayList<String> tableNames = service.getTableNames();
             String tableName = object.getClass().getSimpleName().toLowerCase();
             long check = findId(object, tableName, false);
             if (check != 0) {
                 return null;
             }
             StringBuilder builder = new StringBuilder("INSERT INTO " + tableName + " VALUES(null");
             e:
             for (Field field : object.getClass().getDeclaredFields()) {
                 field.setAccessible(true);
                 String fieldType=field.getType().getSimpleName().toLowerCase();
                if (fieldType.equals("int")) {
                    builder.append(",").append(field.get(object));
                } else if (fieldType.equals("string")) {
                    builder.append(",'").append(field.get(object)).append("'");
               } else if (field.getType().getCanonicalName().contains("java")) {
                    return null;
                } else {                    
                    long _id = -1;
                    for (String tname : tableNames) {
                        if (tname.equalsIgnoreCase(fieldType)) {
                            _id = findId((DataSet)field.get(object), tname,true);
                            if (_id == -1) {
                                return null;
                            }
                            builder.append(",").append(_id);
                            continue e;
                        }
                    }
                    if (_id == -1) {
                        return null;
                    }
                }
            }
            builder.append(")");
            System.out.println(builder.toString());
            return builder.toString();
        } catch (IllegalAccessException | IllegalArgumentException iae) {
            iae.printStackTrace();
            return null;
        }
    }

    <T extends DataSet> long findId(T object, String tname, boolean saveobject) {
        try {
            String command = "SELECT*FROM " + tname;
            TExecutor exec = new TExecutor(service.getConnection());
            long _id = exec.execQuery(command, result -> {
                long id = 0;
                boolean isEmpty = true;
                m:
                while (result.next()) {
                    isEmpty = false;
                    s:
                    for (Field field : object.getClass().getDeclaredFields()) {
                        try {
                            if (result.getObject(tname + "_" + field.getName(), field.getType()).equals(field.get(object))) {
                                continue s;
                            } else {
                                continue m;
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
                if (saveobject) {
                    if (id == 0) {
                        service.save((DataSet) object);
                        if (isEmpty) {
                            id = 1;
                        } else {
                            result.last();
                            id = result.getLong(1) + 1;
                        }
                    }
                }
                return id;
            });
            return _id;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return 0;
        }
    }

    String getLoadCommand(long id, Class<? extends DataSet> clazz) {
        String tableName = clazz.getSimpleName().toLowerCase();
        ArrayList<String>tables=new ArrayList<>();
        ArrayList<String>tableNames = service.getTableNames();
        if (!tableNames.contains(tableName)) {
            return null;
        }
        StringBuilder builder = new StringBuilder("SELECT*FROM "+tableName);
       /* s:
        for (Field field : clazz.getDeclaredFields()) {
            String fieldType=field.getType().getSimpleName().toLowerCase();
            if (fieldType.equals("int") || fieldType.equals("string")) {
                continue s;
            } else if (tableNames.contains(fieldType)) {
                builder.append(",").append(fieldType);
                tables.add(fieldType);
            } else {
                return null;
            }
        }*/
        builder.append(" WHERE " + tableName + "_id LIKE " + id);
       /* for (String s : tables) {          
            builder.append(" AND "+tableName+"_"+s+"_id="+s.toLowerCase()+"_id");            
        }  */      
        System.out.println(builder.toString());
        return builder.toString();
    }
    
    public <T extends DataSet> T createObject(ResultSet result, Class<T> clazz){
        try {
            
            Field[] fields = clazz.getFields();
            //System.out.println(Arrays.toString(fields));
            Class<?>[] types = new Class<?>[fields.length];
            for (int i = 0; i < fields.length; i++) {
                types[i] = fields[i].getType();
               // System.out.println(types[i]);
            }
            
            Constructor constructor = clazz.getConstructor(types);
            ResultSetMetaData metadata = result.getMetaData();
            int columns = metadata.getColumnCount();
            if (fields.length != columns) {
                return null;
            }
            Object[] fromTable = new Object[columns];
            fromTable[columns-1] = result.getLong(1);
            s:
            for (int i = 2; i <= columns; i++) {
                String columnName = metadata.getColumnName(i);
                //System.out.println(columnName);
                //System.out.println(metadata.getColumnTypeName(i));
                if (columnName.contains("_id")) {
                    long id = result.getLong(columnName);
                    T object = null;
                    t:
                    for (Field field : fields) {
                        if (columnName.contains(field.getType().getSimpleName().toLowerCase())) {
                            object = service.load(id, (Class<T>) field.getType());
                            break t;
                        }
                    }
                    if (object == null) {
                        return null;
                    }
                    fromTable[i-2] = object;
                } else if (metadata.getColumnTypeName(i).equals("CHAR")) {
                    fromTable[i-2] = result.getString(i);
                } else {
                    fromTable[i-2] = result.getInt(i);
                }
            }

            Object obj=constructor.newInstance(fromTable);
            return (T)obj;
        } catch (SQLException|NoSuchMethodException|
                InvocationTargetException|InstantiationException|
                IllegalAccessException|IllegalArgumentException  sqle) {
            sqle.printStackTrace();
            return null;
        }
    }
    boolean tableExist(Class<?> clazz) {
        ArrayList<String> tableNames = service.getTableNames();
        if (tableNames.contains(clazz.getSimpleName().toLowerCase())) {
            return true;
        }
        return false;
    }
}
