
package ru.otus.elena.dbservice.main;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dataset.tables.TableToWrite;
import ru.otus.elena.dbservice.dbservice.DBService;
import ru.otus.elena.dbservice.dbservice.LoadResult;
import ru.otus.elena.dbservice.message.Message;
import ru.otus.elena.dbservice.message.MessageContainer;

@Component
public class MessageExecutor {
    
    @Autowired
    private DBService service;
    
    public MessageExecutor(){        
    }
    
    protected MessageContainer exec(MessageContainer inputContainer){
        String to=inputContainer.getFrom();
        String from=inputContainer.getTo();
        Message inputMessage=inputContainer.getMessage();
        Message outputMessage=ececuteMessage(inputMessage);
        return new MessageContainer(outputMessage,from, to);
    }
    private <T extends DataSet> Message ececuteMessage(Message message) {
        try {
            if (message.getBaby() != null) {
                return writeObject((Baby) message.getBaby());
            }else if(message.getCompote()!=null){
                return writeObject((Compote) message.getCompote());
            } else if (message.getId() != 0) {
                return readById(message.getTableName(), message.getId());
            } else if (message.getName() != null) {
                return readByName(message.getTableName(), message.getName());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Message("error");
        }
        return new Message("error");
    }

    public <T extends DataSet> Message writeObject(T object) {
        
        ArrayList<String> messages = service.save(object);
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String s : messages) {
            if (first) {
                builder.append(s);
                first = false;
            } else {
                builder.append(",");
                builder.append(s);
            }
        }
        return new Message(builder.toString());
    }

    public <T extends DataSet> Message readById(String tableName, long id) {
        try {
            TableToWrite table = TableToWrite.valueOf(tableName);
            Class<T> clazz = table.getClazz();
            LoadResult obj = (LoadResult) service.loadById(id, clazz);
            if (obj == null) {
                return new Message("has not been read: table=" + tableName + " id=" + id);
            }
            return new Message(obj.toString());
        } catch (Exception ex) {
            return new Message("has not been read: table=" + tableName + " id=" + id + " " + ex.getMessage());
        }
    }

    public <T extends DataSet> Message readByName(String tableName, String name) {
        try {
            TableToWrite table = TableToWrite.valueOf(tableName);
            Class<T> clazz = table.getClazz();

            LoadResult result = (LoadResult)service.loadByName(name, clazz);
            if (result == null) {
                return new Message("has not been read: table=" + tableName + " name=" + name);
            }
            return new Message(result.toString());
        } catch (Exception e) {
            return new Message("has not been read: table=" + tableName + " name=" + name+" "+e.getMessage());
        }
    }
}
