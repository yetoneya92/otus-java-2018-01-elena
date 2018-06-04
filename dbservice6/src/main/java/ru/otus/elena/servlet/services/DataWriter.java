
package ru.otus.elena.servlet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.interfaces.Service;

@Component
public class DataWriter {

    @Autowired
    private Service service;

    public DataWriter() {
    }


    public <T extends DataSet> boolean writeObject(T object) {
        if (service == null) {
            return false;
        }
        int saved=service.save(object);
        return saved!=0;
    }

}
