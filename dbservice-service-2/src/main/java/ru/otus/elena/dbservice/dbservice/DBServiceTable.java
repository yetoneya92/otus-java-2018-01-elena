package ru.otus.elena.dbservice.dbservice;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dataset.tables.Table;

public class DBServiceTable {

    private static DBServiceTable serviceTable;
    private static DBServiceExecution serviceExec;

    private DBServiceTable() {
    }
    
    public static DBServiceTable getServiceTable() {
        if (serviceTable == null) {
            synchronized (DBService.class) {
                if (serviceTable == null) {
                    serviceTable = new DBServiceTable();
                    serviceExec = DBServiceExecution.getServiceExecution();
                    
                }
            }
        }
        return serviceTable;
    }

    public boolean createTable(Class<? extends DataSet> clazz) {
        if (serviceExec.tableExists(clazz)) {
            System.out.println("Table already exists");
            return false;
        }
        String command = getTableCreateCommand(clazz,null);
        if (command != null) {
            return serviceExec.createTable(command);
        }
        return false;
    }

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

    public <T, R extends DataSet> boolean createTable(Class<T> clazz, Class<R> clacc) {
        String command=getTableCreateCommand(clazz,clacc);
        if(command!=null){
            return serviceExec.createTable(command);
        }
        return false;
    }

    protected<T,R extends DataSet> String getTableCreateCommand(Class<T>clazz,Class<R>clacc) {
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
                Type[] fieldArgsTypes = pType.getActualTypeArguments();
                for (Type type : fieldArgsTypes) {
                    //System.out.println(type);
                    Class pclazz = (Class) type;
                    createTable(pclazz, (Class<R>) clazz);
                }                
            } else if (field.getType().getCanonicalName().contains("java")) {
                return null;
            } else {
                builder.append(tableName).append("_").append(field.getType().getSimpleName().toLowerCase() + "_id").append(" BIGINT(20) NOT NULL,");
            }
        }
        if (clacc!=null){
            builder.append(tableName).append("_").append(clacc.getSimpleName().toLowerCase()).append("_id").append(" BIGINT(20) NOT NULL,");
        }
        builder.append("PRIMARY KEY(id))");
        System.out.println(builder.toString());
        return builder.toString();
    }

    public <T extends DataSet> boolean deleteTable(String tableName) {
        try {
            Table table = Table.valueOf(tableName);
            Class<T> clazz = table.getClazz();
            return deleteTable(clazz);
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean deleteTable(Class<? extends DataSet> clazz) {
        if (!serviceExec.tableExists(clazz)) {
            return false;
        }
        String command = "DROP TABLE " + clazz.getSimpleName().toLowerCase();
        return serviceExec.deleteTable(command);
    }

    public boolean deleteAllTables() {
        ArrayList<String> names = getTableNames();
        if (names.isEmpty()) {
            return true;
        }
        ArrayList<String> commands = new ArrayList<>();
        for (String name : names) {
            commands.add("DROP TABLE " + name);
        }
        return serviceExec.deleteAllTables(commands);
    }

    public ArrayList<String> getTableNames() {
        return serviceExec.getTableNames();
    }
}
