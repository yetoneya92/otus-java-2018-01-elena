
package ru.otus.elena.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ru.otus.elena.dbandcache.dataset.base.DataSet;

public class DBCacheImpl<T extends DataSet> implements DBCache {

    private Map<SoftReference<KeyElement>, DataSet> map = new HashMap<>();
    ReferenceQueue referenceQueue = new ReferenceQueue();

    public <T extends DataSet> void put(T object) {
        map.put(new SoftReference<>(new KeyElement(object), referenceQueue), object);
        //System.out.println(map);
    }

    public <T extends DataSet> T get(long id, Class<T> clazz) {
      
        final ArrayList<T> res = new ArrayList<>();
        //System.out.println(map);
        map.forEach((k, v) -> {
            if (k.get() != null) {
                KeyElement element = k.get();
                if (element.getId() == id && element.getClazz().equals(clazz)) {
                    T object = (T) map.get(k);
                    res.add(object);
                }
            }
        });
        if (res.isEmpty()) {
            return null;
        }
        return res.get(0);
    }

    public int getHitCount() {
        return 0;
    }

    public int getMissCount() {
        return 0;
    }

    public void dispose() {
    }
}
