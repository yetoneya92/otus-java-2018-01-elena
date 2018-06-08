
package ru.otus.elena.messages.message;

import java.util.ArrayList;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.servlet.services.DBReader;
import ru.otus.elena.servlet.services.DBWriter;

public class Message<T extends DataSet>{

    private T object;
    private long id;
    private String tableName;
    private String name;
    private String action;
    private User user;
    
    public Message(User user,String action) {
        this.user=user;
        this.action=action;              
    }
    public Message(User user,T object) {
        this.user=user;
        this.object = object;
              
    }
    public Message(User user, String tableName, long id){
        this.user=user;
        
        this.id=id;
        this.tableName=tableName;
    }

    public Message(User user, String tableName, String name) {
        this.user = user;
        
        this.name=name;
        this.tableName = tableName;
    }

    public void exec() {
        if (object != null) {
            writeObject();
            return;
        } else if (id != 0) {
            readById();
            return;
        } else if (name != null) {
            readByName();
            return;
        }
        else if(action!=null)
           doAction(); 
    }

    public void writeObject() {
        
        
        DBWriter writer = DBWriter.getWriter();
        ArrayList<String>messages = writer.writeObject(object);
        for(String s:messages){
            MessageSystem.getMessageSystem().getAnswerMap().get(user).add(s);
        }

    }

    public void readById() {
        DBReader reader = DBReader.getReader();//
        Object obj = reader.readById(tableName, id);
        if (obj == null) {
            MessageSystem.getMessageSystem().getAnswerMap().get(user).add("has not read: id=" + id + ", table=" + tableName);
        } else {
            String str = obj.toString();
            MessageSystem.getMessageSystem().getAnswerMap().get(user).add("read: " + str);

        }
    }

    public void readByName() {
        DBReader reader = DBReader.getReader();//
        Object obj = reader.readByName(tableName, name);
        if (obj == null) {
            MessageSystem.getMessageSystem().getAnswerMap().get(user).add("has not read: name=" + name + ", table=" + tableName);
        } else {
            String str = obj.toString();
            MessageSystem.getMessageSystem().getAnswerMap().get(user).add("read: " + str);

        }
    }

    public void doAction() {
        if (action.equalsIgnoreCase("exit")) {
            MessageSystem.getMessageSystem().removeUser(user);
        } else {
            MessageSystem.getMessageSystem().getAnswerMap().get(user).add("unknown action: " + action);
        }
    }

    public User getUser() {
        return user;
    }


}
