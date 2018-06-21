
package ru.otus.elena.dbservice.dbservice;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import ru.otus.elena.dbservice.dataset.base.DataSet;

public class DBServiceCreator {
        private boolean parametrize;
        private static DBServiceCreator serviceCreator=null;
       
        private DBServiceCreator(){            
        }
        
        public static DBServiceCreator getServiceCreator(){
            if(serviceCreator==null){
                serviceCreator=new DBServiceCreator();
            }
            return serviceCreator;
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
                        ArrayList<R> list=(ArrayList<R>)DBServiceExecution.getServiceExecution().findElements(id, fields[i], (DataSet)object, this);//???
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
                                    element = DBService.getService().loadById(_id, (Class<R>) fields[j].getType());
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
                objects.add(object);
            }
            return objects;
        } catch (SQLException | InstantiationException
                | IllegalAccessException | IllegalArgumentException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    public void setParametrize(boolean parametrize) {
        this.parametrize = parametrize;
    }
}
