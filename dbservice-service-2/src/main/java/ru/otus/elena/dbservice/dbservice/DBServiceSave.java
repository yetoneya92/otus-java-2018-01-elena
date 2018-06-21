
package ru.otus.elena.dbservice.dbservice;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import ru.otus.elena.dbservice.dataset.base.DataSet;

public class DBServiceSave {

    private static DBServiceSave serviceSave;
    private static DBServiceExecution serviceExec;
    private static DBServiceTable serviceTable;       
    
    private DBServiceSave(){       
    }

    public static DBServiceSave getServiceSave() {
        if (serviceSave == null) {
            synchronized (DBService.class) {
                if (serviceSave == null) {
                    serviceSave=new DBServiceSave();
                    serviceExec = DBServiceExecution.getServiceExecution();
                    serviceTable = DBServiceTable.getServiceTable();
                    
                }
            }
        }
        return serviceSave;
    }

    synchronized <T extends DataSet> ArrayList<String> createSaveCommand(ArrayList<T> list, ArrayList<String> messages) {
        setId(list, messages);
        ArrayList<String>commands = new ArrayList<>();
        for (T object : list) {
            String command = createCommand(object, messages,commands);
            if (command != null) {
                commands.add(command);
            }
        }
        return commands;
    }

    private <T extends DataSet> void setId(ArrayList<T> objectList, ArrayList<String> messages) {
        Iterator<T> iterator = objectList.iterator();        
        while (iterator.hasNext()) {
            T object = iterator.next();
            String tableName = object.getClass().getSimpleName().toLowerCase();
            long check = serviceExec.findInTables(object, tableName, false);
            if (check >0) {
                object.setId(check);
            }
            else if (check == 0) {
                iterator.remove();
                messages.add("alredy exists: " + object.toString());
            } else if (check == -1) {
                iterator.remove();
                messages.add("error: " + object.toString());
            }
        }
    }

    private <T extends DataSet> void setIdifParametrize(ArrayList<T> objectList, ArrayList<String> messages) {
        if (!objectList.isEmpty()) {
            String tableName = objectList.get(0).getClass().getSimpleName().toLowerCase();
            long last=serviceExec.findLast(tableName);
            for(int i=0;i<objectList.size();i++){
                objectList.get(i).setId(last+i+1);
            }
        }
    }

    private <T extends DataSet> String createCommand(T object, ArrayList<String> messages, ArrayList<String> commands) {
        String tableName = object.getClass().getSimpleName().toLowerCase();
        StringBuilder builder = new StringBuilder("INSERT INTO " + tableName + " VALUES(null");
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
                    commands.addAll(getSaveGenericCommands(object, field, tableName, messages,commands));
                } else if (field.getType().getName().contains("java")) {
                    messages.add("has not saved: " + object.toString());
                    return null;
                } else {
                    ArrayList<String> tableNames = DBServiceTable.getServiceTable().getTableNames();
                    System.out.println(tableNames);
                    String tname = field.get(object).getClass().getSimpleName().toLowerCase();
                    long _id=-1;
                    if (!tableNames.contains(tname)) {
                        boolean isCreated = DBServiceTable.getServiceTable().createTable((Class<? extends DataSet>) field.get(object).getClass());
                        if (isCreated) {
                            System.out.println("table " + fieldType + " bas been created");
                            messages.add("table " + fieldType + " bas been created");
                            _id=1;
                        } else {
                            messages.add("has not saved: " + object.toString());
                            return null;
                        }
                    } else {
                        _id = serviceExec.findInTables((DataSet) field.get(object), tname, true);
                    }
                    if (_id == -1) {
                        System.out.println("has not been saved: " + field.get(object).toString());
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
        messages.add(object.toString());
        System.out.println(builder.toString());        
        return builder.toString();
    }

    synchronized <T extends DataSet> ArrayList<String> getSaveGenericCommands(T object, Field field, String tableName, ArrayList<String> messages, ArrayList<String> commands) throws IllegalArgumentException, IllegalAccessException {
        ArrayList<T> objectList = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        if (field.get(object) instanceof Collection) {
            Collection coll = (Collection) field.get(object);
            objectList.addAll(coll);
            setIdifParametrize(objectList, messages);           
            objectList.forEach(s -> {
                String command = createCommand(s, messages, commands);
                int len=command.length();
                command=command.substring(0, len-1)+","+object.getId()+")";
                list.add(command);
            });
            return list;
        } else {
            return list;
        }
    }
 
}
