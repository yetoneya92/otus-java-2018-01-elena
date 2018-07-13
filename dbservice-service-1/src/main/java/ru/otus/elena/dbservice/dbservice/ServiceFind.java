
package ru.otus.elena.dbservice.dbservice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.execution.SaveExecution;

@Service
public class ServiceFind {
    
    private final SaveExecution serviceExecution;
    
    @Autowired
    public ServiceFind(SaveExecution serviceExecution) {
        this.serviceExecution = serviceExecution;
    }
    
       
    public <T extends DataSet> void extractAllObject(ArrayList<T> objectList) throws IllegalArgumentException, IllegalAccessException {
        ArrayList<T> additional = new ArrayList<>();
        Iterator iterator=objectList.iterator();
        while(iterator.hasNext()){
            T object=(T) iterator.next();
            boolean savable=extract(object, additional);
            if(!savable){
                iterator.remove();
            }
        }
        objectList.addAll(additional);
    }

    private <T extends DataSet> boolean extract(T object, ArrayList<T> additional) throws IllegalArgumentException, IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (!field.getType().getName().contains("java")) {
                if (field.get(object) instanceof DataSet) {
                    T newObject = (T) field.get(object);
                    additional.add(newObject);
                    extract(newObject, additional);
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    public <T extends DataSet> void removeDuplicates(ArrayList<T> objectList, ArrayList<String> messages) {
        Iterator<T> iterator = objectList.iterator();        
        while (iterator.hasNext()) {
            T object = iterator.next();
            String tableName = object.getClass().getSimpleName().toLowerCase();
            long check = serviceExecution.findInTables(object, tableName);
            if (check > 0) {
                iterator.remove();
                messages.add("alredy exists: " + object.toString());
            } else if (check == -1) {
                iterator.remove();
                messages.add("error: " + object.toString());
            }
        }
    }

}
