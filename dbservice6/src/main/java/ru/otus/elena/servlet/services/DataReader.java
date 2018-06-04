
package ru.otus.elena.servlet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dataset.tables.TableToWrite;
import ru.otus.elena.dbservice.interfaces.Service;

@Component
public class DataReader <T extends DataSet> {
    @Autowired
    private Service service;
    public DataReader() {

    }

    public Object readById(String tableName, long id) {
        try {
            TableToWrite table = TableToWrite.valueOf(tableName);
            Class<T> clazz = table.getClazz();
            Object obj = service.loadById(id, clazz);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }


    public Object readByName(String tableName,String name) {
        try {
            TableToWrite table = TableToWrite.valueOf(tableName);
            Class<T> clazz = table.getClazz();
            Object obj = service.loadByName(name, clazz);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }
}
