
package ru.otus.elena.dbservice.dbservice;

import ru.otus.elena.dbservice.execution.SaveExecution;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.elena.dbservice.dataset.base.DataSet;

@Service
public class ServiceLoad {       
    @Autowired
    public ServiceLoad() {        
    }
    public String getLoadCommand(long id, Class<? extends DataSet> clazz, ArrayList<String> messages) {

        StringBuilder builder = new StringBuilder("SELECT*FROM " + clazz.getSimpleName().toLowerCase());
        builder.append(" WHERE id LIKE " + id);
        System.out.println(builder.toString());
        return builder.toString();
    }

    public String getLoadCommand(String name, Class<? extends DataSet> clazz, ArrayList<String> messages) {

        String tableName = clazz.getSimpleName().toLowerCase();
        String command = "SELECT*FROM " + tableName + " WHERE " + tableName + "_name LIKE \"" + name + "\"";
        return command;
    }

    public String getLoadCommand(Class<? extends DataSet> clazz, ArrayList<String> messages) {

        String tableName = clazz.getSimpleName().toLowerCase();
        String command = "SELECT*FROM " + tableName;
        return command;
    }

}
