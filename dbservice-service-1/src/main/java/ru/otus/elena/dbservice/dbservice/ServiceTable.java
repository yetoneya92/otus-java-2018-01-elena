package ru.otus.elena.dbservice.dbservice;

import ru.otus.elena.dbservice.execution.ServiceExecution;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dataset.tables.Table;

@Component
public class ServiceTable {   
    private final static ArrayList<String>tableNames=new ArrayList<>();
    @Autowired
    private ServiceExecution serviceExecution;

    public ServiceTable() {
    }


    public boolean tableExists(Class<? extends DataSet> clazz) {
        if (tableNames.contains(clazz.getSimpleName().toLowerCase())) {
            return true;
        } else {
            tableNames.clear();
            tableNames.addAll(getTableNames());
            if (tableNames.contains(clazz.getSimpleName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean createTable(Class<? extends DataSet> clazz, ArrayList<String> messages) {
        if (serviceExecution.tableExists(clazz)) {
            messages.add("Table " + clazz.getSimpleName() + " already exists");
            return false;

        }
        String command = getTableCreateCommand(clazz, null);
        if (command != null) {
            return serviceExecution.createTable(command);
        }
        messages.add("Table "+clazz.getSimpleName()+" has not been created");
        return false;
    }

    public <T extends DataSet> boolean createTable(String tableName, ArrayList<String> messages) {
        try {
            Table table = Table.valueOf(tableName);
            Class<T> clazz = table.getClazz();
            return createTable(clazz, messages);
        } catch (Exception ex) {
            messages.add("Table "+tableName+" has not been created: "+ex.getMessage());
            return false;
        }
    }

    public <T, R extends DataSet> boolean createTable(Class<T> clazz, Class<R> clacc) {
        String command = getTableCreateCommand(clazz, clacc);
        if (command != null) {
            return serviceExecution.createTable(command);
        }
        return false;
    }

    protected <T, R extends DataSet> String getTableCreateCommand(Class<T> clazz, Class<R> clacc) {
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
        if (clacc != null) {
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
        if (!serviceExecution.tableExists(clazz)) {
            return false;
        }
        String command = "DROP TABLE " + clazz.getSimpleName().toLowerCase();
        return serviceExecution.deleteTable(command);
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
        return serviceExecution.deleteAllTables(commands);
    }

    public ArrayList<String> getTableNames() {
        ArrayList<String> tables = serviceExecution.getTableNames();
        tableNames.clear();
        tableNames.addAll(tables);
        return tables;
    }
}
