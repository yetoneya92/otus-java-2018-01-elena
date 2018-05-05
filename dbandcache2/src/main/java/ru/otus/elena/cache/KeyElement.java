
package ru.otus.elena.cache;

import java.util.Objects;
import ru.otus.elena.dbandcache.dataset.base.DataSet;

public class KeyElement<T extends DataSet> {

    long id;
    Class<T> clazz;

    public KeyElement(T object) {
        this.id = object.getId();
        this.clazz = (Class<T>) object.getClass();
    }
    public KeyElement(long id,Class<T>clazz){
        this.id=id;
        this.clazz=clazz;
    }

    public long getId() {
        return id;
    }

    public Class<?> getClazz() {
        return clazz;
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
