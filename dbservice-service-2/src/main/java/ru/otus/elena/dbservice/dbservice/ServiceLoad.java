
package ru.otus.elena.dbservice.dbservice;

import ru.otus.elena.dbservice.execution.ServiceExecution;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.base.DataSet;

@Component
public class ServiceLoad {

    @Autowired
    private ServiceExecution serviceExecution;
    public ServiceLoad(){
        
    }

    String getLoadCommand(long id, Class<? extends DataSet> clazz, ArrayList<String> messages) {
        if (!tableExists(clazz, messages)) {
            return null;
        }
        StringBuilder builder = new StringBuilder("SELECT*FROM " + clazz.getSimpleName().toLowerCase());
        builder.append(" WHERE id LIKE " + id);
        System.out.println(builder.toString());
        return builder.toString();
    }

    String getLoadCommand(String name, Class<? extends DataSet> clazz, ArrayList<String> messages) {
        if (!tableExists(clazz, messages)) {
            return null;
        }
        String tableName = clazz.getSimpleName().toLowerCase();
        String command = "SELECT*FROM " + tableName + " WHERE " + tableName + "_name LIKE \"" + name + "\"";
        return command;
    }

    String getLoadCommand(Class<? extends DataSet> clazz, ArrayList<String> messages) {
        if (!tableExists(clazz, messages)) {
            return null;
        }
        String tableName = clazz.getSimpleName().toLowerCase();
        String command = "SELECT*FROM " + tableName;
        return command;
    }

    private boolean tableExists(Class<? extends DataSet> clazz, ArrayList<String> messages) {
        String tableName = clazz.getSimpleName().toLowerCase();
        ArrayList<String> tables = new ArrayList<>();
        ArrayList<String> tableNames = serviceExecution.getTableNames();
        if (tableNames.contains(tableName)) {
            return true;
        }
        messages.add("Table "+clazz.getSimpleName().toLowerCase()+" doesn't exists");
        return false;
    }
}
