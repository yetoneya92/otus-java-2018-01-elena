
package ru.otus.elena.dbservice.dbservice;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import ru.otus.elena.dbservice.executors.TExecutor;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.cache.DBCache;

public class DBServiceSearcher {

    private DBServiceSelf service;
    private DBServiceHelper helper;
    private DBCache cache;
    
    public DBServiceSearcher(DBServiceSelf service, DBServiceHelper helper) {
        this.service = service;
        this.helper=helper;      
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
                    if(helper.getParametrize()){break m;}
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
                                long findInTables = findId((DataSet) field.get(object), field.get(object).getClass().getSimpleName().toLowerCase(), true);
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
                        helper.setLast(id);
                    } else {
                        System.out.println(object.toString() + " exists");
                    }
                }
                else {
                    if (isEmpty) {
                        helper.setLast(0);
                    } else {
                        helper.setLast(result.getLong(1));
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
    
    <T, R extends DataSet> ArrayList<R> findElements(long id, Field field, T object) throws SQLException {
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
                return helper.createObjects(result, pclazz, (DataSet) object);
            });
            helper.setParametrize(false);
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

    public void setCache(DBCache cache) {
        this.cache = cache;
    }
}
