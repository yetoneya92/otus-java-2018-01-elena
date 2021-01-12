
package ru.otus.elena.dbservice.dataset.tables;

import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dataset.base.DataSet;

  public enum Table {
    baby {
        @Override
        public <T extends DataSet> Class<T> getClazz() {
            return (Class<T>) Baby.class;
        }
    },
    compote {
        @Override
        public <T extends DataSet> Class<T> getClazz() {
            return (Class<T>) Compote.class;
        }
    },
    fruit {
        @Override
        public <T extends DataSet> Class<T> getClazz() {
            return (Class<T>) Fruit.class;
        }
    },
    phone {
    @Override
    public <T extends DataSet> Class<T> getClazz(){return (Class<T>) Phone.class;}
    };
    public abstract<T extends DataSet> Class<T> getClazz();
}
 