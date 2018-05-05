
package ru.otus.elena.cache;

import ru.otus.elena.dbandcache.dataset.base.DataSet;

public interface DBCache {

    public <T extends DataSet> T get(long id,Class<T>clazz);

    public <T extends DataSet> void put(T object);

    int getHitCount();

    int getMissCount();

    void dispose();
}
