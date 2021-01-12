
package ru.otus.elena.dbservice.dbservice;

import java.util.ArrayList;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.execution.SaveBaby;
import ru.otus.elena.dbservice.execution.SaveCompote;
import ru.otus.elena.dbservice.execution.ServiceExecution;

@Component
public class SaveStandard {
    @Autowired
    private  SaveBaby saveBaby;
    @Autowired
    private  SaveCompote saveCompote;
    @Autowired
    private ServiceTable serviceTable;
    
    public SaveStandard(){
        
    }

    public <T extends DataSet> int saveStandard(ArrayList<T> objectList, ArrayList<String> messages) {
        int savedBabies=0;
        int savedCompotes=0;
        if (!objectList.isEmpty()) {
            ArrayList<Baby> babies = new ArrayList<>();
            ArrayList<Compote> compotes = new ArrayList<>();
            Iterator<T> iterator = objectList.iterator();
            s:
            while (iterator.hasNext()) {
                T object = iterator.next();
                if (object.getClass().equals(Baby.class)) {
                    babies.add((Baby) object);
                    iterator.remove();
                } else if (object.getClass().equals(Compote.class)) {
                    compotes.add((Compote) object);
                    iterator.remove();
                }
                else if(object.getClass().equals(Phone.class)){
                    iterator.remove();
                    messages.add("has not been saved: "+object.toString());
                }
                else if(object.getClass().equals(Fruit.class)){
                    messages.add("has not been saved: "+object.toString());
                }
            }
            if (!babies.isEmpty()) {
                if(!serviceTable.tableExists(Baby.class)){
                    serviceTable.createTable(Baby.class, messages);
                    serviceTable.createTable(Phone.class, messages);
                }
                saveBaby.removeDuplicate(babies, messages);
                savedBabies = saveBaby.save(babies, messages);
            }
            if (!compotes.isEmpty()) {
                if (!serviceTable.tableExists(Compote.class)) {
                    serviceTable.createTable(Compote.class, messages);
                    serviceTable.createTable(Fruit.class, messages);
                }
                saveCompote.removeDuplicate(compotes, messages);
                savedCompotes = saveCompote.save(compotes, messages);
            }
        }
        return savedBabies + savedCompotes;
    }
    

}
