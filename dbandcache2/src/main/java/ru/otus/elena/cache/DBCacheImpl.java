
package ru.otus.elena.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import ru.otus.elena.dbandcache.dataset.base.DataSet;

public class DBCacheImpl<T extends DataSet> implements DBCache {
    private int hitCount;
    private int missCount;
    private Map<KeyElement, SoftReference<T>> map = new ConcurrentHashMap<>();
    ReferenceQueue referenceQueue = new ReferenceQueue();

    public <T extends DataSet> void put(T object) {
        map.put(new KeyElement(object),new SoftReference<>(object,referenceQueue));       
        //System.out.println(map);
    }

    public <T extends DataSet> T get(long id, Class<T> clazz) {

        
        KeyElement key = new KeyElement(id, clazz);
        T object = (T) map.get(key).get();
        if (object != null) {
            if (hitCount < Integer.MAX_VALUE) {
                hitCount++;
            } else {
                hitCount = 0;
            }
            return object;
        }
        if (hitCount < Integer.MAX_VALUE) {
            missCount++;
        } else {
            missCount = 0;
        }
        return null;

    }

    public int getHitCount() {
        return hitCount;
    }

    public int getMissCount() {
        return missCount;
    }

    public void dispose() {
        map.clear();
    }
}
