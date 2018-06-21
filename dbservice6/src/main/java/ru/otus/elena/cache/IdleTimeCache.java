package ru.otus.elena.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.base.DataSet;

@Component
public class IdleTimeCache<T extends DataSet> implements Cache {
    private static final int TIME_THRESHOLD_MS = 5;
    
    private int maxElement;   
    private long idleTimeMs;
    private final Map<KeyElement,DataSet> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();
    private int hit = 0;
    private int miss = 0;       
    public IdleTimeCache(){}
    
    public IdleTimeCache(int maxElement,long idleTimeMs) {
        this.maxElement = maxElement;       
        this.idleTimeMs = idleTimeMs;       
    }
    
    public void setMaxElement(int maxElement){
        this.maxElement=maxElement;
    }
    
    public void setIdleTimeMs(long idleTimeMs){
       this.idleTimeMs=idleTimeMs;
    }
    

    public <T extends DataSet> void put(T object) {
        if (elements.size() >= maxElement) {
            KeyElement firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        KeyElement key=new KeyElement(object);
        key.setAccessed();
        elements.put(key, object);
        TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
        timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);

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
            key.setAccessed();
        } else {
            miss++;
        }
        return element;
    }


    @Override
    public int getMaxElements() {
        return maxElement;
    }

    @Override
    public int getCacheSize() {
        return elements.size();
    }
    
    public int getHitCount() {
        return hit;
    }

    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private <T extends DataSet> TimerTask getTimerTask(final KeyElement key, Function<KeyElement, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                T element = (T) elements.get(key);
                if (element == null || timeFunction.apply(key) < System.currentTimeMillis() + TIME_THRESHOLD_MS) {
                    elements.remove(key);
                    this.cancel();
                }
            }
        };
    }

}