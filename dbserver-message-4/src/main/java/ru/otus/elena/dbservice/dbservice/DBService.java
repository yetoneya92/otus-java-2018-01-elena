
package ru.otus.elena.dbservice.dbservice;

import ru.otus.elena.dbservice.execution.ServiceExecution;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.dbservice.cache.DBCache;

@Component
public class DBService implements Service{
    
    @Autowired
    private ServiceExecution serviceExecution;
    @Autowired
    private ServiceTable serviceTable;
    @Autowired
    private ServiceSave serviceSave;
    @Autowired
    private ServiceLoad serviceLoad;
    @Autowired
    private SaveStandard saveStandard;
    @Autowired
    private LoadStandard loadStandard;
    @Autowired
    private ServiceConnection serviceConnection;
    private DBCache cache;


    public DBService() {        
    }

    @Override
    public void setCache(DBCache cache) {
        this.cache = cache;        
    }

    public DBCache getCache() {
        return cache;
    }
    
    @Override
    public <T extends DataSet> ArrayList<String> createTable(String tableName) {
        ArrayList<String> messages = new ArrayList<>();
        boolean isCreated = serviceTable.createTable(tableName, messages);
        if (isCreated) {
            messages.add("Table " + tableName + " has been created");
        }
        return messages;
    }

    @Override
    public ArrayList<String> createTable(Class<? extends DataSet> clazz) {
        ArrayList<String> messages = new ArrayList<>();
        boolean isCreated = serviceTable.createTable(clazz, messages);
        if (isCreated) {
            messages.add("Table " + clazz.getSimpleName().toLowerCase() + " has been created");
        }
        return messages;
    }

    @Override
    public ArrayList<String> getTableNames() {
        return serviceTable.getTableNames();
    }

    @Override
    public<T extends DataSet> boolean deleteTable(String tableName){
        return serviceTable.deleteTable(tableName);
    }

    @Override
    public boolean deleteTable(Class<? extends DataSet> clazz) {
       return serviceTable.deleteTable(clazz);
    }

    @Override
    public boolean deleteAllTables() {
       return serviceTable.deleteAllTables();
    }

    @Override
    public <T extends DataSet> ArrayList<String> saveAll(ArrayList<T> objectList) {
        int saved = 0;
        ArrayList<T> initial = new ArrayList<>(objectList);
        ArrayList<String> messages = new ArrayList<>();
        if (!objectList.isEmpty()) {
            for (T object : objectList) {
                if (!serviceTable.tableExists(object.getClass())) {
                    createTable(object.getClass());
                }
            }
            saved = saveStandard.saveStandard(objectList, messages);            
            if (!objectList.isEmpty()) {
                ArrayList<CommandContainer>commands = serviceSave.createSaveCommand(objectList, messages);                                
                if (commands != null && commands.size() > 0) {                                        
                    saved = serviceExecution.saveAll(commands);
                }
            }
            initial.forEach(s -> {
                if (s.getId() != 0) {
                    messages.add("object has been saved: " + s.toString());
                }
            });
            messages.add("saved objects=" + saved);
            if (cache != null) {
                initial.forEach(s -> {
                    if (s.getId() != 0) {
                        cache.put(s);
                    }
                });
            }
        }
        return messages;
    }

    @Override
    public <T extends DataSet> ArrayList<String> save(T object) {
        ArrayList<T> objectList = new ArrayList<>();
        objectList.add(object);
        return saveAll(objectList);
    }

    @Override
    public <T extends DataSet> LoadResult loadById(long id, Class<T> clazz) {
        ArrayList<String> messages = new ArrayList<>();
        T data = null;
        if (cache != null) {
            data = cache.get(id, clazz);
            if (data != null) {
                return new LoadResult(data, messages);
            }
        }
        ArrayList<T> dataList = loadStandard.loadById(id, clazz);
        if (dataList == null) {
            String command = serviceLoad.getLoadCommand(id, clazz, messages);
            if (command == null) {
                messages.add("has not been read: id=" + id + ", class=" + clazz.getSimpleName());
                return new LoadResult(data, messages);
            }
            ArrayList<T> objects = serviceExecution.load(command, clazz);
            if (objects == null || objects.isEmpty()) {
                messages.add("has not been read: id=" + id + ", class=" + clazz.getSimpleName());
                return new LoadResult(data, messages);
            }
            return new LoadResult(objects.get(0), messages);
        } else if (dataList.isEmpty()) {
            messages.add("has not been loaded: id=" + id + ", class=" + clazz.getSimpleName());
            return new LoadResult(data, messages);
        } else {
            messages.add("load: id=" + id + ", class=" + clazz.getSimpleName());
            return new LoadResult(dataList.get(0), messages);
        }

    }

    @Override
    public <T extends DataSet> LoadResult loadByName(String name, Class<T> clazz) {
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<T> objectList = loadStandard.loadByName(name, clazz);
        if (objectList == null) {
            String command = serviceLoad.getLoadCommand(name, clazz, messages);
            if (command == null) {
                messages.add("has not been read: name=" + name + ", class=" + clazz.getSimpleName());
                T data = null;
                return new LoadResult(data, messages);
            }
            objectList = serviceExecution.load(command, clazz);
            if (objectList.isEmpty() || objectList == null) {
                messages.add("has not been read: name=" + name + ", class=" + clazz.getSimpleName());
            }
            return new LoadResult(objectList, messages);
        }
        if(objectList.isEmpty()){
            messages.add("has not been read: name=" + name + ", class=" + clazz.getSimpleName());
            return new LoadResult(objectList, messages);
        }
        return new LoadResult(objectList, messages);
    }

    @Override
    public <T extends DataSet> LoadResult load(Class<T> clazz) {
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<T> objectList = loadStandard.load(clazz);
        if (objectList == null) {
            String command = serviceLoad.getLoadCommand(clazz, messages);
            return new LoadResult(serviceExecution.load(command, clazz),messages);
        }
        return new LoadResult(objectList, messages);
    }

    @Override
    public void shutDown() {
        serviceConnection.shutDown();
    }

}
