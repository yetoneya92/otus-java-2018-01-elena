package ru.otus.elena.dbservice.dbservice;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.cache.DBCache;

public class ServiceCommand {
    private static ServiceCommand serviceCommand=null;
    private static ServiceExecution serviceExec;
    private boolean parametrize;
    private long last;
    private long objectId;
    private int parameterCounter;
    private DBCache cache;
    private boolean useCache;

    private ServiceCommand() {
    }

    public static ServiceCommand getServiceCommand() {
        if (serviceCommand == null) {
            serviceCommand=new ServiceCommand();
            serviceExec=ServiceExecution.getServiceExecution();
        }
        return serviceCommand;
    }
    public void setCache(DBCache cache) {
        this.cache = cache;
        useCache = true;
    }

    protected String getTableCreateCommand(Class<? extends DataSet> clazz) {
        String tableName = clazz.getSimpleName().toLowerCase();
        StringBuilder builder = new StringBuilder("CREATE TABLE " + tableName + "(id BIGINT(20) NOT NULL AUTO_INCREMENT,");
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType().equals(int.class)) {
                builder.append(tableName).append("_").append(field.getName().toLowerCase()).append(" INT(20),");
            } else if (field.getType().equals(String.class)) {
                builder.append(tableName).append("_").append(field.getName().toLowerCase()).append(" CHAR(25),");
            } else if (field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) field.getGenericType();
                //System.out.println(pType);
                Type[] fieldArgsTypes = pType.getActualTypeArguments();
                for (Type type : fieldArgsTypes) {
                    System.out.println(type);
                    Class pclazz = (Class) type;
                    ServiceSelf.getService().createTable(pclazz);
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

    synchronized <T extends DataSet> ArrayList<String> getSaveCommand(ArrayList<T> list, ArrayList<String>messages) {
        try {
            ArrayList<String> commands = new ArrayList<>();
            if (useCache && !parametrize) {
                serviceExec.removeDublicate(list);
            }
            m:
            for (T object : list) {
                String tableName = object.getClass().getSimpleName().toLowerCase();
                long check = 0;
                if (!parametrize || parameterCounter == 1) {
                    check = serviceExec.findId(object, tableName, false, this);
                }
                if (check == -1) {
                    System.out.println("has not saved: " + object.toString());
                    messages.add("has not saved: " + object.toString());
                    continue m;
                }
                if (!parametrize) {
                    if (check != 0) {
                        System.out.println("already exists: " + object.toString());
                        messages.add("already exists: " + object.toString());
                        continue m;
                    }
                    objectId = last + 1;
                    object.setId(objectId);
                } else {
                    object.setId(last + parameterCounter++);

                }

                StringBuilder builder = new StringBuilder("INSERT INTO " + tableName + " VALUES(null");
                e:
                for (Field field : object.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    String fieldType = field.getType().getSimpleName().toLowerCase();
                    if (fieldType.equals("int")) {
                        builder.append(",").append(field.get(object));
                    } else if (fieldType.equals("string")) {
                        builder.append(",'").append(field.get(object)).append("'");
                    } else if (field.getGenericType() instanceof ParameterizedType) {
                        parametrize = true;
                        commands.addAll(getSaveGenericCommands(object, field, tableName,messages));

                        /* } else if (field.getType().getCanonicalName().contains("java")) {
                    System.out.println("has not saved"+object.toString());
                    continue m;*/
                    } else {

                        if (!serviceExec.tableExists((Class<? extends DataSet>) field.get(object).getClass())) {
                            ServiceSelf.getService().createTable((Class<? extends DataSet>) field.get(object).getClass());                            
                            System.out.println("table " + fieldType + " bas been created");
                            messages.add("table " + fieldType + " bas been created");
                        }
                        if (parametrize) {

                            //System.out.println("LAST+"+last);
                            builder.append(",").append(objectId);
                        } else {
                            long _id = 0;
                            ArrayList<String> tableNames = ServiceSelf.getService().getTableNames();
                            for (String tname : tableNames) {
                                if (tname.equals(fieldType)) {
                                    _id = serviceExec.findId((DataSet) field.get(object), tname, true, this);
                                    if (_id == -1) {
                                        System.out.println("error: " + object.toString());
                                        messages.add("error: " + object.toString());
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

    synchronized <T extends DataSet> ArrayList<String> getSaveGenericCommands(T object, Field field, String tableName, ArrayList<String> messages) throws IllegalArgumentException, IllegalAccessException {
        parameterCounter = 1;
        if (field.get(object) instanceof Collection) {
            Collection coll = (Collection) field.get(object);
            ArrayList<T> objects = new ArrayList<>();
            objects.addAll(coll);
            ArrayList<String> list = getSaveCommand(objects, messages);
            //System.out.println(list);          
            parametrize = false;
            return list;
        } else {
            return null;
        }
    }

    String getLoadCommand(long id, Class<? extends DataSet> clazz) {
        String tableName = clazz.getSimpleName().toLowerCase();
        ArrayList<String> tables = new ArrayList<>();
        ArrayList<String> tableNames = serviceExec.getTableNames();
        if (!tableNames.contains(tableName)) {
            return null;
        }
        StringBuilder builder = new StringBuilder("SELECT*FROM " + tableName);
        builder.append(" WHERE id LIKE " + id);
        System.out.println(builder.toString());
        return builder.toString();
    }

    public void setParametrize(boolean parametrize) {
        this.parametrize = parametrize;
    }

    public boolean getParametrize() {
        return parametrize;
    }

    public void setLast(long last) {
        this.last = last;
    }

    public void setId(long objectId) {
        this.objectId = objectId;
    }

}
