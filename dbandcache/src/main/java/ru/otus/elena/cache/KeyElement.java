
package ru.otus.elena.cache;

import ru.otus.elena.dbandcache.dataset.base.DataSet;

public class KeyElement<T extends DataSet> {
    long id;
    Class<T> clazz;
    public KeyElement(T object){
       this.id=object.getId();
       this.clazz=(Class<T>)object.getClass();
    }
    public long getId(){
        return id;
    }
    public Class<?> getClazz(){
        return clazz;
    }
}
