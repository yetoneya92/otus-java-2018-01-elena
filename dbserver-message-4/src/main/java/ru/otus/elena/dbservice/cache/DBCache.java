
package ru.otus.elena.dbservice.cache;

import ru.otus.elena.dbservice.dataset.base.DataSet;

public interface DBCache {

    public <T extends DataSet> T get(long id,Class<T>clazz);

    public <T extends DataSet> void put(T object);

    int getHitCount();

    int getMissCount();

    void dispose();
    
    int getCacheSize();
    
    int getMaxElements();
}
