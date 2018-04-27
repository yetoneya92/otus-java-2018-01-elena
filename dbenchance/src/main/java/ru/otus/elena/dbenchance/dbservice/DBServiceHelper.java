
package ru.otus.elena.dbenchance.dbservice;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ru.otus.elena.dbenchance.interfaces.DataSet;

import ru.otus.elena.dbenchance.executors.TExecutor;

public class DBServiceHelper {
    private DBServiceNative service;
    private boolean parametrize;
    private long last;
    public DBServiceHelper(DBServiceNative service){
        this.service=service;
    }
    
        protected String getTableCreateCommand(Class<? extends DataSet>clazz) {
        String tableName=clazz.getSimpleName().toLowerCase();
        StringBuilder builder=new StringBuilder
        ("CREATE TABLE "+tableName+"(id BIGINT(20) NOT NULL AUTO_INCREMENT,");        
        for(Field field:clazz.getDeclaredFields()){
            field.setAccessible(true);
            if (field.getType().equals(int.class)) {
                builder.append(tableName).append("_").append(field.getName().toLowerCase()).append(" INT(20),");
                } else if (field.getType().equals(String.class)) {
                    builder.append(tableName).append("_").append(field.getName().toLowerCase()).append(" CHAR(25),");
                } else if (field.getGenericType() instanceof ParameterizedType) {                                    
                    ParameterizedType pType=(ParameterizedType)field.getGenericType();
                    System.out.println(pType);
                    Type[]fieldArgsTypes=pType.getActualTypeArguments();
                    for(Type type:fieldArgsTypes){
                        System.out.println(type);
                        Class pclazz=(Class)type;
                        service.createTable(pclazz);
                    }
                                   
                } else if (field.getType().getCanonicalName().contains("java")) {
                    return null;
                } else {
                    builder.append(tableName).append("_").append(field.getType().getSimpleName().toLowerCase() + "_id").append(" BIGINT(20) NOT NULL,");
                }
            }
        builder.append("PRIMARY KEY(id))");
            System.out.println(builder.toString());
        return builder.toString();
    }

     synchronized <T extends DataSet> ArrayList<String> getSaveCommand(T... data){   
         try {
            ArrayList<String> commands = new ArrayList<>();          
             m:
             for (T object : data) {
                 if (!tableExists(object.getClass())) {
                     service.createTable(object.getClass());
                 }
                 String tableName = object.getClass().getSimpleName().toLowerCase();
                 if(!parametrize){
                 long check = findId(object, tableName, false, false);
                 if (check == -1) {
                     System.out.println("has not saved: " + object.toString());
                     continue m;
                 } else if (check != 0) {
                     System.out.println("already exists: " + object.toString());
                     continue m;
                 }}
                 
                 StringBuilder builder = new StringBuilder("INSERT INTO " + tableName + " VALUES(null");
                 e:
             for (Field field : object.getClass().getDeclaredFields()) {
                 field.setAccessible(true);
                 String fieldType=field.getType().getSimpleName().toLowerCase();
                if (fieldType.equals("int")) {
                    builder.append(",").append(field.get(object));
                } else if (fieldType.equals("string")) {
                    builder.append(",'").append(field.get(object)).append("'");
                }else if(field.getGenericType() instanceof ParameterizedType){
                    parametrize=true;
                    commands.addAll(saveGenericCommand(object,field,tableName));
                    
              /* } else if (field.getType().getCanonicalName().contains("java")) {
                    System.out.println("has not saved"+object.toString());
                    continue m;*/
                } else {
                   
                    if (!tableExists((Class<? extends DataSet>) field.get(object).getClass())) {
                        service.createTable((Class<? extends DataSet>) field.get(object).getClass());
                        System.out.println("table "+fieldType+" bas been created");
                    }
                     if (parametrize) {
                         
                         System.out.println("LAST+"+last);
                         builder.append(",").append(last);
                     } else {
                         long _id = 0;
                         ArrayList<String> tableNames = service.getTableNames();
                         for (String tname : tableNames) {
                             if (tname.equals(fieldType)) {
                                 _id = findId((DataSet) field.get(object), tname, true, false);
                                 if (_id == -1) {
                                     System.out.println("error: " + object.toString());
                                     continue m;
                                 }
                                 builder.append(",").append(_id);
                                 continue e;
                             }
                         }
                     }
                 }
                 }
                 builder.append(")");
                 System.out.println(builder.toString());
                 commands.add(builder.toString());
             }
            return commands;
        } catch (IllegalAccessException | IllegalArgumentException iae) {
            iae.printStackTrace();
            return null;
        }
    }

    synchronized <T extends DataSet> ArrayList<String> saveGenericCommand(T object, Field field, String tableName) throws IllegalArgumentException, IllegalAccessException {
        ArrayList<String>commands=new ArrayList<>();
        last = findId(object, tableName, false, true);
        if(field.get(object) instanceof Collection){
            Collection coll=(Collection)field.get(object);
            System.out.println(coll);
            coll.forEach(s -> {
                System.out.println(s);
                ArrayList<String>list=getSaveCommand((DataSet)s);
                System.out.println(list);
                commands.addAll(list);
            });
           
            parametrize=false;
            return commands;
        } else {
            return null;
       }
    }


    <T extends DataSet> long findId(T object, String tname, boolean saveobject, boolean last) {
        try {
            String command = "SELECT*FROM " + tname;
            TExecutor exec = new TExecutor(service.getConnection());
            long _id = exec.execQuery(command, result -> {
                long id = 0;
                boolean isEmpty = true;
               
                m:
                while (result.next()) {                   
                    isEmpty = false;
                    if (last) {
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
                                long findInTables = findId((DataSet) field.get(object), field.get(object).getClass().getSimpleName().toLowerCase(), true, false);
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
                        service.save((DataSet) object);
                        if (isEmpty) {
                            id = 1;
                        } else {
                            id = result.getLong(1) + 1;

                        }
                    } else {
                        System.out.println(object.toString() + " exists");
                    }
                }
                if (last) {
                    if (isEmpty) {
                        id = 1;
                    } else {
                        id = result.getLong(1)+1;
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

    String getLoadCommand(long id, Class<? extends DataSet> clazz) {
        String tableName = clazz.getSimpleName().toLowerCase();
        ArrayList<String>tables=new ArrayList<>();
        ArrayList<String>tableNames = service.getTableNames();
        if (!tableNames.contains(tableName)) {
            return null;
        }
        StringBuilder builder = new StringBuilder("SELECT*FROM "+tableName);
        builder.append(" WHERE id LIKE " + id);          
        System.out.println(builder.toString());
        return builder.toString();
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
                long id=result.getLong(1);
                fields[fields.length - 1].setLong(object,id);

                for (int i = 0; i < fields.length - 1; i++) {
                    if (fields[i].getGenericType() instanceof ParameterizedType) {
                        parametrize = true;
                        
                        Set<R> coll = new HashSet<>();
                        ArrayList<R> list=(ArrayList<R>)findElements(id, fields[i], (DataSet)object);
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
                                    element = service.loadById(_id, (Class<R>) fields[j].getType());
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
                                if(metadata.getColumnTypeName(i).equals("INT")&&fields[j].getType().equals(int.class)){
                                    fields[j].set(object, result.getInt(i));
                                } else if (metadata.getColumnTypeName(i).equals("CHAR")&&fields[j].getType().equals(String.class)) {
                                    fields[j].set(object, result.getString(i));
                                }
                            }
                        }
                    }
                }
                //System.out.println(object);
                objects.add(object);
            }
            return objects;
        } catch (SQLException | InstantiationException
                | IllegalAccessException | IllegalArgumentException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    <T,R extends DataSet> ArrayList<R> findElements(long id, Field field, T object) throws SQLException {             
        ParameterizedType pType = (ParameterizedType) field.getGenericType();
        //System.out.println(pType);
        Type[] fieldArgsTypes = pType.getActualTypeArguments();
        for (Type type : fieldArgsTypes) {
            //System.out.println(type);
            Class pclazz = (Class) type;
            String tableName = pclazz.getSimpleName().toLowerCase();
            String objectName = object.getClass().getSimpleName().toLowerCase();
            String command = "SELECT*FROM " + tableName + " WHERE " + tableName + "_" + objectName + "_id LIKE " + id;
            TExecutor exec = new TExecutor(service.getConnection());
            ArrayList<R> elements = (ArrayList<R>) exec.execQuery(command, result -> {
                return createObjects(result, pclazz, (DataSet) object);
            });
            parametrize = false;
            return elements;
        }
        return null;
    }

    boolean tableExists(Class<?> clazz) {
        ArrayList<String> tableNames = service.getTableNames();
        if (tableNames.contains(clazz.getSimpleName().toLowerCase())) {
            return true;
        }
        return false;
    }
}
