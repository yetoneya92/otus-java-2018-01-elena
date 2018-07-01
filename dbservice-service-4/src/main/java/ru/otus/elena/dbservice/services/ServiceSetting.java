
package ru.otus.elena.dbservice.services;

import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dbservice.DBService;

@Component
public class ServiceSetting {
    
    private String login;
    private boolean isTesting;
    private boolean isStarted;
    
    
    public ServiceSetting(){         
    }
 
    public void setLogin(String login) {
        this.login = login;
    }
    public String getLogin() {
        return login;
    }

    public void setTesting(boolean isTesting) {
        this.isTesting = isTesting;
    }

    public boolean isTesting() {
        return isTesting;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }
    public boolean isStarted(){
        return isStarted;
    }
    
}
