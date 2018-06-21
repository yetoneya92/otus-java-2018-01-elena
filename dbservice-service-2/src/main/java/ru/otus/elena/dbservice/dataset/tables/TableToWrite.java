
package ru.otus.elena.dbservice.dataset.tables;

import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.base.DataSet;

public enum TableToWrite {
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
    };

    public abstract <T extends DataSet> Class<T> getClazz();
}
