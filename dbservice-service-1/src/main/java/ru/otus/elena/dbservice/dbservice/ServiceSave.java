
package ru.otus.elena.dbservice.dbservice;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dataset.tables.Table;

@Service
public class ServiceSave {

    public ServiceSave(){
    }

    
    public synchronized <T extends DataSet> ArrayList<CommandContainer> createSaveCommand(ArrayList<T> objectList, ArrayList<String> messages) {        
        Collections.reverse(objectList);
        ArrayList<CommandContainer> commands = new ArrayList<>();
        for (T object : objectList) {
            CommandContainer container = createCommand(object, messages);
            if (container != null) {
                commands.add(container);
            }
        }
        return commands;
    }

    protected <T extends DataSet> CommandContainer createCommand(T object, ArrayList<String> messages) {
        String tableName = object.getClass().getSimpleName().toLowerCase();
        for (Table table : Table.values()) {
            if (table.name().equalsIgnoreCase(tableName)) {
                return null;
            }
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
                    T newObject=(T) field.get(object);
                    builder.append(",").append(newObject.getId());
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
