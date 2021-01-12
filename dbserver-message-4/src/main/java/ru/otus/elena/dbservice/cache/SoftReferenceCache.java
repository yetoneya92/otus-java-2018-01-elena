
package ru.otus.elena.dbservice.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

import ru.otus.elena.dbservice.dataset.base.DataSet;

public class SoftReferenceCache<T extends DataSet> implements DBCache {
    private int hitCount;
    private int missCount;
    private int maxElements;
    private Map<KeyElement, SoftReference<T>> elements = new LinkedHashMap<>();
    ReferenceQueue referenceQueue = new ReferenceQueue();

    public SoftReferenceCache(int maxElements) {
        this.maxElements = maxElements;
    }

    @Override
    public <T extends DataSet> void put(T object) {
        if (elements.size() == maxElements) {
            KeyElement firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }
        elements.put(new KeyElement(object), new SoftReference<>(object, referenceQueue));
        //System.out.println(map);
    }

    @Override
    public <T extends DataSet> T get(long id, Class<T> clazz) {      
        KeyElement key = new KeyElement(id, clazz);
        T object = (T) elements.get(key).get();
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
   @Override
    public int getCacheSize() {
        return elements.size();
    }
    @Override
    public int getMaxElements() {
        return maxElements;
    }

    @Override
    public int getHitCount() {
        return hitCount;
    }

    @Override
    public int getMissCount() {
        return missCount;
    }

    @Override
    public void dispose() {

    }
}
