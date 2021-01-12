
package ru.otus.elena.dbservice.dbservice;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.execution.LoadBaby;
import ru.otus.elena.dbservice.execution.LoadCompote;

@Service
public class LoadStandard {
    
    private final LoadBaby loadBaby;   
    private final LoadCompote loadCompote;
    
    @Autowired
    public LoadStandard(LoadBaby loadBaby,LoadCompote loadCompote) {
        this.loadBaby=loadBaby;
        this.loadCompote=loadCompote;
    }


    public <T extends DataSet> ArrayList<T> loadById(long id, Class<T> clazz) {
        ArrayList<T> list = new ArrayList<>();
        if (clazz.equals(Baby.class)) {            
            Baby baby = loadBaby.loadByBabyId(id);
            if (baby != null) {
                list.add((T) baby);
            }
            return list;
        } else if (clazz.equals(Compote.class)) {
            Compote compote=loadCompote.loadById(id);
            if(compote!=null){
                list.add((T) compote);
            }
            return list; 
        } 
        return null;
    }

    public <T extends DataSet> ArrayList<T> loadByName(String name, Class<T> clazz) {
        if (clazz.equals(Baby.class)) {
            return (ArrayList<T>) loadBaby.loadByName(name);
        } else if (clazz.equals(Compote.class)) {
            return (ArrayList<T>) loadCompote.loadByName(name);
        }
        return null;
    }

    public <T extends DataSet> ArrayList<T> load(Class<T> clazz) {
        if(clazz.equals(Baby.class)){
            return (ArrayList<T>) loadBaby.load();
        }
        else if(clazz.equals(Compote.class)){
            return (ArrayList<T>)loadCompote.load();
        }
        return null;
    }
}
