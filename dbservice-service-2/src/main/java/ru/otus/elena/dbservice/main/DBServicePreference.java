
package ru.otus.elena.dbservice.main;

import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.dbservice.services.DBTest;

public class DBServicePreference {
    
    private String login;
    private Service service;
    private DBTest test;
    private static DBServicePreference preference;
    
    private DBServicePreference(){       
    }

    public static DBServicePreference getDBServicePreference() {
        if (preference == null) {
            synchronized (DBServicePreference.class) {
                if (preference == null) {
                    preference = new DBServicePreference();
                }
            }
        }
        return preference;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    public void setDBTest(DBTest test) {
        this.test = test;
    }

    public DBTest getDBTest() {
        return test;
    }   
}
