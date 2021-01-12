package ru.otus.elena.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.function.Function;
import ru.otus.elena.dbservice.dataset.base.DataSet;

public class LifeTimeCache<T extends DataSet> implements ServiceCache {

    private static final int TIME_THRESHOLD_MS = 5;

    private final int maxElements;
    private final long lifeTimeMs;

    private final Map<KeyElement, DataSet> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public LifeTimeCache(int maxElements, long lifeTimeMs) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs;

    }

    @Override
    public <T extends DataSet> void put(T object) {
        if (elements.size() == maxElements) {
            KeyElement firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        KeyElement key = new KeyElement(object);
        elements.put(key, object);

        TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
        timer.schedule(lifeTimerTask, lifeTimeMs);

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
            //key.setAccessed();
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
