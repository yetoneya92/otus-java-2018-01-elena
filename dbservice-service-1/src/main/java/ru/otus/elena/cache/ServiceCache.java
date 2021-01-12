
package ru.otus.elena.cache;

import ru.otus.elena.dbservice.dataset.base.DataSet;

public interface ServiceCache {

    public <T extends DataSet> T get(long id,Class<T>clazz);

    public <T extends DataSet> void put(T object);

    int getHitCount();

    int getMissCount();

    void dispose();
    
    int getCacheSize();
    
    int getMaxElements();
}
