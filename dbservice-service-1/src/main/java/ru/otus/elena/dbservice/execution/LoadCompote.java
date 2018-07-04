package ru.otus.elena.dbservice.execution;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.cache.DBCache;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dbservice.DBService;
import ru.otus.elena.dbservice.dbservice.ServiceConnection;
import ru.otus.elena.dbservice.executors.TExecutor;

@Component
public class LoadCompote {

    @Autowired
    private DBService service;
    @Autowired
    private ServiceConnection serviceConnection;

    public Compote loadById(long id) {
        Compote compote = findCompoteInCache(id);
        if (compote != null) {
            return compote;
        }
        String command = "select compote.id, compote_name, fruit.id, fruit_name, "
                + "fruit_number from compote, fruit where compote.id like " + id + " and compote.id=fruit_compote_id";
        return loadCompote(command);
    }

    public ArrayList<Compote> loadByName(String name) {
        String command = "select compote.id, compote_name, fruit.id, fruit_name, "
                + "fruit_number from compote, fruit where compote_name like \"" + name + "\" and compote.id=fruit_compote_id";
        System.out.println(command);
        ArrayList<Compote> compotes = new ArrayList<>();
        compotes.add(loadCompote(command));
        return compotes;
    }

    public ArrayList<Compote> load() {
        String command = "select compote.id, compote_name, fruit.id, fruit_name, "
                + "fruit_number from compote, fruit where compote.id=fruit_compote_id";;
        try {
            TExecutor exec = new TExecutor(serviceConnection.getConnection());
            ArrayList<Compote> compotes = exec.execQuery(command, result -> {
                ArrayList<Compote> objects = new ArrayList<>();
                Set<Fruit> fruits = null;
                Compote compote=null;
                long compoteId = 0;
                while (result.next()) {
                    long currentCompote = result.getLong(1);
                    if (compoteId != currentCompote) {
                        fruits = new HashSet<>();
                        Fruit fruit = new Fruit(result.getString(4), result.getInt(5), result.getLong(3));
                        fruits.add(fruit);
                        compote = new Compote(result.getString(2), fruits, result.getLong(1));
                        objects.add(compote);
                        compoteId = currentCompote;
                    } else {
                        Fruit fruit = new Fruit(result.getString(4), result.getInt(5), result.getLong(3));
                        compote.getFruit().add(fruit);
                    }
                }
                result.close();
                return objects;
            });
            return compotes;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    private Compote loadCompote(String command) {
        try {
            TExecutor exec = new TExecutor(serviceConnection.getConnection());
            Compote compote = exec.execQuery(command, result -> {
                Set<Fruit> fruits = new HashSet<>();
                Compote obj = new Compote();
                while (result.next()) {
                    obj.setName(result.getString(2));
                    obj.setId(result.getLong(3));
                    Fruit fruit = new Fruit(result.getString(4), result.getInt(5), result.getLong(3));
                    fruits.add(fruit);                    
                }
                obj.setFruit(fruits);
                result.close();
                return obj;
            });
            return compote;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    private Compote findCompoteInCache(long id) {
        DBCache cache = service.getCache();
        if (cache != null) {
            Compote compote = cache.get(id, Compote.class);
            return compote;
        }
        return null;
    }

}
