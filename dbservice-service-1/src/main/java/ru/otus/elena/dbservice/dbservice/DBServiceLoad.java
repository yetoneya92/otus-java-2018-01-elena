
package ru.otus.elena.dbservice.dbservice;

import java.util.ArrayList;
import ru.otus.elena.dbservice.dataset.base.DataSet;

public class DBServiceLoad {
    private static DBServiceLoad serviceLoad=null;
    private static DBServiceExecution serviceExec=null;

    public static DBServiceLoad getServiceLoad() {
        if (serviceLoad == null) {
            synchronized (DBService.class) {
                if (serviceLoad == null) {
                    serviceLoad = new DBServiceLoad();
                    serviceExec = DBServiceExecution.getServiceExecution();                   
                }
            }
        }
        return serviceLoad;
    }

    String getLoadCommand(long id, Class<? extends DataSet> clazz) {
        String tableName = clazz.getSimpleName().toLowerCase();
        ArrayList<String> tables = new ArrayList<>();
        ArrayList<String> tableNames = serviceExec.getTableNames();
        if (!tableNames.contains(tableName)) {
            return null;
        }
        StringBuilder builder = new StringBuilder("SELECT*FROM " + tableName);
        builder.append(" WHERE id LIKE " + id);
        System.out.println(builder.toString());
        return builder.toString();
    }

    String getLoadCommand(String name, Class<? extends DataSet> clazz){
        return null;
    }

}
