
package ru.otus.elena.dbservice.execution;

import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dbservice.ServiceConnection;
import ru.otus.elena.dbservice.executors.TExecutor;

@Service
public class LoadBaby {
  
    private ServiceConnection serviceConnection;
    
    @Autowired
    public LoadBaby(ServiceConnection serviceConnection){
        this.serviceConnection=serviceConnection;
    }

    public Baby loadByBabyId(long id) {
        String command = "select baby.id, baby_name, phone.id, phone_cellphone,phone_homephone from baby, phone where baby.id like " + id + " and phone.id=baby_phone_id";
        return loadBaby(command);
    }
    // не используется
    public Baby loadByPhoneId(long id) {
        String command = "select baby.id, baby_name, phone.id, phone_cellphone,phone_homephone from baby, phone where phone.id like " + id + " and phone.id=baby_phone_id";
        return loadBaby(command);
    }

    public ArrayList<Baby> loadByName(String name) {
        String command = "select baby.id, baby_name, phone.id, phone_cellphone,phone_homephone from baby, phone where baby_name like \"" + name + "\" and phone.id=baby_phone_id";
        return loadBabies(command);
    }

    public ArrayList<Baby> load() {
        String command = "select baby.id, baby_name, phone.id, phone_cellphone,phone_homephone from baby, phone where phone.id=baby_phone_id";
        return loadBabies(command);
    }

    private Baby loadBaby(String command) {
        try {
            TExecutor exec = new TExecutor(serviceConnection.getConnection());
            Baby baby = exec.execQuery(command, result -> {
                Baby obj = null;
                while (result.next()) {
                    obj = new Baby();
                    obj.setId(result.getLong(1));
                    obj.setName(result.getString(2));
                    Phone phone = new Phone(result.getInt(4), result.getInt(5),result.getLong(3));
                    obj.setPhone(phone);
                    break;
                }
                result.close();
                return obj;
            });
            return baby;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    private ArrayList<Baby> loadBabies(String command) {
        try {
            TExecutor exec = new TExecutor(serviceConnection.getConnection());
            ArrayList<Baby> babies = exec.execQuery(command, result -> {
                ArrayList<Baby> objects = new ArrayList<>();
                while (result.next()) {
                    Baby obj = new Baby();
                    obj.setId(result.getLong(1));
                    obj.setName(result.getString(2));
                    Phone phone = new Phone(result.getInt(4),result.getInt(5), result.getLong(3));
                    obj.setPhone(phone);
                    objects.add(obj);
                }
                result.close();
                return objects;
            });
            return babies;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }


}
