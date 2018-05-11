
package ru.otus.elena.cache;

import java.util.Objects;
import ru.otus.elena.dbandcache.dataset.base.DataSet;

public class KeyElement<T extends DataSet> {
    private  long creationTime;
    private long lastAccessTime;
    long id;
    Class<T> clazz;

    public KeyElement(T object) {
        this.id = object.getId();
        this.clazz = (Class<T>) object.getClass();
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }
    public KeyElement(long id,Class<T>clazz){
        this.id=id;
        this.clazz=clazz;
        
    }
    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }
    public long getId() {
        return id;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clazz);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        KeyElement other = (KeyElement) obj;
        return Objects.equals(getId(), other.getId()) && Objects.equals(getClazz(), other.getClazz());
    }

    @Override
    public String toString() {
        return "id=" + id + "class=" + clazz;

    }
}
