package ru.otus.elena.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.function.Function;
import ru.otus.elena.dbandcache.dataset.base.DataSet;

public class EternalCache<T extends DataSet> implements DBCache {    
    
    private final int maxElements;
    private final Map<KeyElement,DataSet> elements = new LinkedHashMap<>();   
    private int hit = 0;
    private int miss = 0;

    public EternalCache(int maxElements) {
        this.maxElements = maxElements;

    }

    public <T extends DataSet> void put(T object) {
        if (elements.size() == maxElements) {
            KeyElement firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        KeyElement key = new KeyElement(object);
        elements.put(key, object);

    }

    @Override
    public <T extends DataSet> T get(long id, Class<T> clazz) {
        KeyElement key = new KeyElement(id, clazz);
        return get(key);
    }

    public <T extends DataSet> T get(KeyElement key) {
        T element = (T) elements.get(key);
        if (element != null) {
            hit++;
        } else {
            miss++;
        }
        return element;
    }
    @Override
    public int getCacheSize() {
        return elements.size();
    }

    @Override
    public int getMaxElements() {
        return maxElements;
    }

    public int getHitCount() {
        return hit;
    }

    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {

    }


 


}
