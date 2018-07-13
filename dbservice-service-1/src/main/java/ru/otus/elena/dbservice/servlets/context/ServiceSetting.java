
package ru.otus.elena.dbservice.servlets.context;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
