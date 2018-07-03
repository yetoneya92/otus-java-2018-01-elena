
package ru.otus.elena.dbservice.dbservice;

import ru.otus.elena.dbservice.execution.ServiceExecution;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dataset.tables.Table;

@Component
public class ServiceSave {
    @Autowired
    private ServiceSave serviceSave;
    @Autowired
    private ServiceExecution serviceExecution;
    @Autowired
    private ServiceTable serviceTable;       
    
    public ServiceSave(){       
    }


    synchronized <T extends DataSet> ArrayList<CommandContainer> createSaveCommand(ArrayList<T> list, ArrayList<String> messages) {
        removeDuplicates(list, messages);
        ArrayList<CommandContainer>commands = new ArrayList<>();
        for (T object : list) {
            CommandContainer container = createCommand(object, messages);
            if (container != null) {
                commands.add(container);
            }
        }
        return commands;
    }

    private <T extends DataSet> void removeDuplicates(ArrayList<T> objectList, ArrayList<String> messages) {
        Iterator<T> iterator = objectList.iterator();        
        while (iterator.hasNext()) {
            T object = iterator.next();
            String tableName = object.getClass().getSimpleName().toLowerCase();
            long check = serviceExecution.findInTables(object, tableName, false);
            if (check > 0) {
                iterator.remove();
                messages.add("alredy exists: " + object.toString());
            } else if (check == -1) {
                iterator.remove();
                messages.add("error: " + object.toString());
            }
        }
    }


    protected <T extends DataSet> CommandContainer createCommand(T object, ArrayList<String> messages) {
        String tableName = object.getClass().getSimpleName().toLowerCase();
        for (Table table : Table.values()) {
            if (table.name().equalsIgnoreCase(tableName)) {
                return null;
            }
        }
        if(!serviceTable.tableExists(object.getClass())){
            serviceTable.createTable(object.getClass(), messages);
        }
        StringBuilder builder = new StringBuilder("INSERT INTO " + tableName + " VALUES(null");
        ArrayList<CommandContainer>genericCommand=new ArrayList<>();
        try {
            e:
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldType = field.getType().getSimpleName().toLowerCase();
                if (fieldType.equals("int")) {
                    builder.append(",").append(field.get(object));
                } else if (fieldType.equals("string")) {
                    builder.append(",'").append(field.get(object)).append("'");
                } else if (field.getGenericType() instanceof ParameterizedType) {
                    genericCommand.addAll(getSaveGenericCommands(object, field, tableName, messages));
                } else if (field.getType().getName().contains("java")) {
                    messages.add("has not saved: " + object.toString());
                    return null;
                } else {
                    ArrayList<String> tableNames = serviceTable.getTableNames();
                    String tname = field.get(object).getClass().getSimpleName().toLowerCase();                    
                    if (!tableNames.contains(tname)) {
                        boolean isCreated = serviceTable.createTable((Class<? extends DataSet>) field.get(object).getClass(), messages);
                        if (isCreated) {
                            System.out.println("table " + fieldType + " bas been created");
                        } else {
                            messages.add("has not saved: " + object.toString());
                            return null;
                        }
                    }
                    long _id=serviceExecution.findInTables((DataSet) field.get(object), tname, true);
                    if (_id == -1) {
                        messages.add("has not been saved: " + field.get(object).toString());
                        return null;
                    }                    
                    builder.append(",").append(_id);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            System.out.println(ex.getMessage());
        }        
        builder.append(")");
        //System.out.println(builder.toString());
        return new CommandContainer(object,builder.toString(),genericCommand);
    }
    
    synchronized <T extends DataSet> ArrayList<CommandContainer> getSaveGenericCommands(T object, Field field, String tableName, ArrayList<String> messages) 
            throws IllegalArgumentException, IllegalAccessException {
        ArrayList<T> objectList = new ArrayList<>();
        CommandContainer[] container=new CommandContainer[1];
        ArrayList<CommandContainer>containerList=new ArrayList<>();
        if (field.get(object) instanceof Collection) {
            Collection coll = (Collection) field.get(object);
            objectList.addAll(coll);
            
            objectList.forEach(s -> {
                
                container[0]=createCommand(s, messages);

                containerList.add(container[0]);
            });
        }
        return containerList;
    }

}
