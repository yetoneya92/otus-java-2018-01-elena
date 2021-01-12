
package ru.otus.elena.dbservice.execution;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dbservice.CommandContainer;
import ru.otus.elena.dbservice.dbservice.ServiceConnection;
import ru.otus.elena.dbservice.executors.SimpleExecutor;
import ru.otus.elena.dbservice.executors.TExecutor;
import ru.otus.elena.dbservice.executors.TransactionExecutor;

@Service
public class SaveExecution {
        
    private final ServiceConnection serviceConnection;
    private final String databaseName = "db_example";

    @Autowired
    public SaveExecution(ServiceConnection serviceConnection) {
        this.serviceConnection = serviceConnection;
    }

    public ArrayList<String> getTableNames() {
        try {
            ArrayList<String> tableNames = new ArrayList<>();
            try (ResultSet result = serviceConnection.getConnection().getMetaData().getTables(databaseName, "", "%", null)) {
                while (result.next()) {
                    tableNames.add(result.getString("TABLE_NAME"));
                }
                result.close();
                return tableNames;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    public boolean tableExists(Class<?> clazz) {
        ArrayList<String> tableNames = getTableNames();
        if (tableNames.contains(clazz.getSimpleName().toLowerCase())) {
            return true;
        }
        return false;
    }    
    
    public boolean createTable(String command) {
        try {
            if (command != null) {
                SimpleExecutor exec = new SimpleExecutor(serviceConnection.getConnection());
                int update = exec.execUpdate(command);
                return true;
            } else {
                return false;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    public boolean deleteTable(String command) {
        try {
            SimpleExecutor exec = new SimpleExecutor(serviceConnection.getConnection());
            int update = exec.execUpdate(command);
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }
    public boolean deleteAllTables(ArrayList<String> commands) {
        try {
            TransactionExecutor exec = new TransactionExecutor(serviceConnection.getConnection());
            exec.execUpdate(commands);
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    public <T extends DataSet> int saveAll(ArrayList<CommandContainer> commands) {
        
        TExecutor exec = new TExecutor(serviceConnection.getConnection());
        commands.forEach(s -> {
            System.out.println(s.getCommand());
            try {
                long[] id = new long[1];
                exec.execUpdate(s.getCommand(), result -> {
                    while (result.next()) {
                        id[0] = result.getLong(1);
                        s.getObject().setId(id[0]);
                        break;
                    }
                });
                ArrayList<CommandContainer> list = s.getGeneric();
                if (list != null && !list.isEmpty()) {
                    list.forEach(m -> {
                        String command = m.getCommand();
                        int len = command.length();
                        command = command.substring(0, len - 1) + "," + s.getObject().getId() + ")";
                        m.setCommand(command);
                    });
                    saveAll(list);
                }

            } catch (SQLException ex) {
                Logger.getLogger(SaveExecution.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return 1;
    }

    public <T extends DataSet> long findLast(String tableName) {
        try {
            String command = "SELECT*FROM " + tableName;
            TExecutor exec = new TExecutor(serviceConnection.getConnection());
            long _id = exec.execQuery(command, result -> {
                long id = 0;
                while (result.next()) {
                    result.last();
                    id = result.getLong(1);
                }
                result.close();
                return id;
            });
            return _id;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        }
    }

    public <T extends DataSet> long findInTables(T object, String tableName) {
        try {
            String command = "SELECT*FROM " + tableName;
            TExecutor exec = new TExecutor(serviceConnection.getConnection());
            long _id = exec.execQuery(command, result -> {
                long id = 0;
                m:
                while (result.next()) {
                    s:
                    for (Field field : object.getClass().getDeclaredFields()) {
                        try {
                            if (field.getType().equals(int.class) || field.getType().equals(String.class)) {
                                if (result.getObject(tableName + "_" + field.getName().toLowerCase(), field.getType()).equals(field.get(object))) {
                                    continue s;
                                } else {
                                    continue m;
                                }
                            } else if (field.getGenericType() instanceof ParameterizedType) {
                                continue s;
                            } else {
                                long find = findInTables((DataSet) field.get(object), field.get(object).getClass().getSimpleName().toLowerCase());//from tables
                                long current = result.getObject(tableName + "_" + field.getName().toLowerCase() + "_id", long.class);//from result
                                if (find == current) {
                                    continue s;
                                } else {
                                    continue m;
                                }
                            }
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            ex.printStackTrace();
                            id = -1;
                            break m;
                        }
                    }
                    id = result.getLong(1);
                    
                    break m;
                }
                result.close();
                return id;
            });
            return _id;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return -1;
        }
    }
}
