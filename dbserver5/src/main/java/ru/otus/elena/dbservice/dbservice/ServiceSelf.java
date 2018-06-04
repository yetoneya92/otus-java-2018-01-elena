
package ru.otus.elena.dbservice.dbservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.cache.DBCache;
import ru.otus.elena.dbservice.dataset.tables.Table;
import ru.otus.elena.dbservice.interfaces.Service;


public class ServiceSelf implements Service{
    
    private static volatile ServiceSelf service=null;
    private static ServiceExecution serviceExec=null;
    private static ServiceCommand serviceComm=null;
    public boolean useCache;
    public DBCache cache;


    private ServiceSelf() {
        
    }
 
    public static ServiceSelf getService() {
        if (service == null) {
            service = new ServiceSelf();
            serviceExec=ServiceExecution.getServiceExecution();
            serviceComm=ServiceCommand.getServiceCommand();
        } 
            return service;
        
    }
    
        public static ServiceSelf getService(String host,String databaseName,String user,String password) {
        if (service == null) {
            service = new ServiceSelf();
            serviceExec=ServiceExecution.getServiceExecution(host,databaseName,user,password);
            serviceComm=ServiceCommand.getServiceCommand();
        } 
            return service;
        
    }
   
    @Override
    public void setCache(DBCache cache) {
       this.useCache=true;
       this.cache=cache;
       ServiceCommand.getServiceCommand().setCache(cache);
       ServiceExecution.getServiceExecution().setCache(cache);
    }


    @Override
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

    @Override
    public boolean createTable(Class<? extends DataSet> clazz) {
        if (serviceExec.tableExists(clazz)) {
            System.out.println("Table already exists");
            return false;
        }
        String command = serviceComm.getTableCreateCommand(clazz);
        if (command != null) {
            return serviceExec.createTable(command);
        }
        return false;
    }
    
    @Override
    public ArrayList<String> getTableNames() {
        return serviceExec.getTableNames();
    }

    @Override
    public<T extends DataSet> boolean deleteTable(String tableName){
        try{Table table=Table.valueOf(tableName);       
            Class<T>clazz=table.getClazz();
            boolean isDelete=deleteTable(clazz);
            return isDelete;}
        catch(Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteTable(Class<? extends DataSet> clazz) {

        if (!serviceExec.tableExists(clazz)) {
            return false;
        }
        String command = "DROP TABLE " + clazz.getSimpleName().toLowerCase();
        return serviceExec.deleteTable(command);
    }

    @Override
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

    public <T extends DataSet> int saveAll(T... data) {
        for (T object : data) {
            if (!serviceExec.tableExists(object.getClass())) {
                createTable(object.getClass());
            }
        }

        ArrayList<T> objects = new ArrayList<>(Arrays.asList(data));
        ArrayList<String> commands = serviceComm.getSaveCommand(objects);
        if (useCache) {
            objects.forEach(s -> cache.put(s));
        }
        if (commands != null && commands.size() > 0) {
            return serviceExec.saveAll(commands);
        }
        return 0;
    }

    @Override
    public <T extends DataSet> int save(T object) {
        return saveAll(object);
    }


    @Override
    public <T extends DataSet> T loadById(long id, Class<T> clazz) {
        T data = null;
        if (useCache) {
            data = cache.get(id,clazz);
            if (data != null) {
                return data;
            }
        }        
            String command = serviceComm.getLoadCommand(id, clazz);
            if (command == null) {
                return null;
            }
            return serviceExec.loadById(command, clazz);
        
    }

    @Override
    public void shutDown(){
           service=null;
           serviceExec.shutDown();
    }

    @Override
    public <T extends DataSet> T loadByName(String name, Class<T> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends DataSet> List<T> load(Class<T> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
