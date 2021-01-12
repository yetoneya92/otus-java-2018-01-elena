
package ru.otus.elena.dbservice.interfaces;

import java.util.ArrayList;
import java.util.List;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.cache.DBCache;
import ru.otus.elena.dbservice.dbservice.LoadResult;

public interface Service {

    public <T extends DataSet> ArrayList<String> createTable(String tableName);
    
    public ArrayList<String> createTable(Class<? extends DataSet> clazz);    
    
    public ArrayList<String> getTableNames();
    
    public <T extends DataSet> boolean deleteTable(String tableName);

    public boolean deleteTable(Class<? extends DataSet> clazz);
    
    public boolean deleteAllTables();
    
    public <T extends DataSet> ArrayList<String> saveAll(ArrayList<T>objects);
 
    public <T extends DataSet> ArrayList<String> save(T object);

    public <T extends DataSet> LoadResult loadById(long id, Class<T> clazz);

    public <T extends DataSet> LoadResult loadByName(String name, Class<T> clazz);
   
    public <T extends  DataSet> LoadResult load(Class<T> clazz);
    
    public void setCache(DBCache cache);
    
    public DBCache getCache();

    public void shutDown();
    
    
 
}
