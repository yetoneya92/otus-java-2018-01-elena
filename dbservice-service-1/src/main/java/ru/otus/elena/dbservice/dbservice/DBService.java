
package ru.otus.elena.dbservice.dbservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dataset.tables.Table;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.cache.ServiceCache;


public class DBService implements Service{
    
    private static volatile DBService service;
    private static volatile DBServiceExecution serviceExec;
    private static volatile DBServiceTable serviceTable;
    private static volatile DBServiceSave serviceSave;
    private static volatile DBServiceLoad serviceLoad;
    public boolean useCache;
    public ServiceCache cache;


    private DBService() {
        
    }

    public static DBService getService() {
        if (service == null) {
            return getService("localhost", "db_example", "me", "me");
        }
        return service;
    }

    public static DBService getService(String host, String databaseName, String user, String password) {
        if (service == null) {
            synchronized (DBService.class) {
                if (service == null) {
                    service = new DBService();
                    serviceExec = DBServiceExecution.getServiceExecution(host, databaseName, user, password);
                    serviceTable=DBServiceTable.getServiceTable();
                    serviceLoad=DBServiceLoad.getServiceLoad();
                    serviceSave=DBServiceSave.getServiceSave();
                    

                }
            }
        }
        return service;

    }

    @Override
    public void setCache(ServiceCache cache) {
        this.useCache = true;
        this.cache = cache;        
    }

    public ServiceCache getCache() {
        return cache;
    }
    

    @Override
    public <T extends DataSet> boolean createTable(String tableName) {
        return serviceTable.createTable(tableName);
    }

    @Override
    public boolean createTable(Class<? extends DataSet> clazz) {
        return serviceTable.createTable(clazz);
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

    public <T extends DataSet> ArrayList<String> saveAll(T... data) {
        ArrayList<T> listObjects = new ArrayList<>(Arrays.asList(data));
        if(useCache){
            findDublicateInCache(listObjects);
        }
        for (T object : data) {
            if (!serviceExec.tableExists(object.getClass())) {
                createTable(object.getClass());
            }
        }
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<String> commands = serviceSave.createSaveCommand(listObjects, messages);
        if (commands != null && commands.size() > 0) {
            int saved = serviceExec.saveAll(commands);
            if (saved != 0) {
                messages.add("saved objects=" + saved);
                if (useCache) {
                    listObjects.forEach(s -> cache.put(s));
                }
            }
            return messages;
        }
        messages.add("has not been saved");
        return messages;
    }

    @Override
    public <T extends DataSet> ArrayList<String> save(T object) {

        return saveAll(object);
    }

    @Override
    public <T extends DataSet> T loadById(long id, Class<T> clazz) {
        T data = null;
        if (useCache) {
            data = cache.get(id, clazz);
            if (data != null) {
                return data;
            }
        }
        String command = serviceLoad.getLoadCommand(id, clazz);
        if (command == null) {
            return null;
        }
        return serviceExec.loadById(command, clazz);
    }

    @Override
    public <T extends DataSet> T loadByName(String name, Class<T> clazz) {
        String command = serviceLoad.getLoadCommand(name, clazz);
        if (command == null) {
            return null;
        }
        return serviceExec.loadByName(name, clazz);
    }

    @Override
    public <T extends DataSet> List<T> load(Class<T> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public<T extends DataSet>void findDublicateInCache(ArrayList<T>objects){
        Iterator<T> iterator = objects.iterator();
        s:
        while (iterator.hasNext()) {
            T object = iterator.next();
            if (object.getId() == 0) {
                continue s;
            }
            if (cache.get(object.getId(), object.getClass()) != null) {
                iterator.remove();               
            }           
        }
    }
    
    @Override
    public void shutDown() {
        serviceExec.shutDown();
    }


}
