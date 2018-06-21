
package ru.otus.elena.dbservice.main;

import java.util.ArrayList;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.message.Message;
import ru.otus.elena.dbservice.message.MessageContainer;
import ru.otus.elena.dbservice.services.DBReader;
import ru.otus.elena.dbservice.services.DBWriter;
public class MessageExecutor {
    
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
        DBWriter writer = DBWriter.getWriter();
        ArrayList<String> messages = writer.writeObject(object);
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

    public Message readById(String tablename, long id) {
        DBReader reader = DBReader.getReader();//
        Object obj = reader.readById(tablename, id);
        if (obj == null) {
            return new Message("has not been read: table=" + tablename + " id=" + id);
        }
        return new Message(obj.toString());
    }

    public Message readByName(String tablename, String name) {
        DBReader reader = DBReader.getReader();//
        Object obj = reader.readByName(tablename, name);
        if (obj == null) {
            return new Message("has not been read: table=" + tablename + " name=" + name);
        }
        return new Message(obj.toString());
    }
}
