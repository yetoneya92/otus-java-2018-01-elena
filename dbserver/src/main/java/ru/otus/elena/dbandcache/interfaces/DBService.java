
package ru.otus.elena.dbandcache.interfaces;

import ru.otus.elena.cache.DBCache;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import ru.otus.elena.dbandcache.dataset.base.DataSet;

public interface DBService {

    

    public void createTable(Class<? extends DataSet> clazz);    
    
    public ArrayList<String> getTableNames();

    public void deleteTable(Class<? extends DataSet> clazz);
    
    public void deleteAllTables();
    
    public <T extends DataSet> int saveAll(T... data);
    
    public <T extends DataSet>  void save(T object);

    public <T extends DataSet> T loadById(long id, Class<T> clazz);
    
    public <T extends DataSet> T loadByName(String name, Class<T>clazz);
    
    public <T extends  DataSet> List<T> load(Class<T> clazz);
    
    public void setCache(DBCache cache);

    public void shutDown();
 
}
