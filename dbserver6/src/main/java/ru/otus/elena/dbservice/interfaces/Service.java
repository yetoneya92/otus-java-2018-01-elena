
package ru.otus.elena.dbservice.interfaces;

import java.util.ArrayList;
import java.util.List;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.cache.Cache;

public interface Service {

    public <T extends DataSet> boolean createTable(String tableName);
    
    public boolean createTable(Class<? extends DataSet> clazz);    
    
    public ArrayList<String> getTableNames();
    
    public <T extends DataSet> boolean deleteTable(String tableName);

    public boolean deleteTable(Class<? extends DataSet> clazz);
    
    public boolean deleteAllTables();
    
    public <T extends DataSet> int saveAll(T... data);
    
    public <T extends DataSet>  int save(T object);

    public <T extends DataSet> T loadById(long id, Class<T> clazz);
    
    public <T extends DataSet> T loadByName(String name, Class<T>clazz);
    
    public <T extends  DataSet> List<T> load(Class<T> clazz);
    
    public void setCache(Cache cache);

    public void shutDown();
 
}
