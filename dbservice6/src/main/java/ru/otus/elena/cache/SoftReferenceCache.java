
package ru.otus.elena.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.base.DataSet;

@Component
public class SoftReferenceCache<T extends DataSet> implements Cache {
    private int hitCount;
    private int missCount;
    private int maxElement;
    private Map<KeyElement, SoftReference<T>> elements = new LinkedHashMap<>();
    ReferenceQueue referenceQueue = new ReferenceQueue();
    
    public SoftReferenceCache(){} 

    public SoftReferenceCache(int maxElements) {
        this.maxElement = maxElements;
    }
    
    public void setMaxElement(int maxElement){
       this.maxElement=maxElement;
    }

    @Override
    public <T extends DataSet> void put(T object) {
        if (elements.size() == maxElement) {
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
        return maxElement;
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
