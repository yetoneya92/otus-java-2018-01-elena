
package ru.otus.elena.dbservice.websocket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dbservice.DBService;

public class MessageExecutor {
    
    @Autowired
    DBService service;
    public String execute(String message){
        String[]tokens=message.split(",");
        System.out.println(Arrays.toString(tokens));
        if(tokens[0].equalsIgnoreCase("read")){
            return executeRead(tokens);
        }
        else if(tokens[0].equalsIgnoreCase("write")){
            return executeWrite(tokens);
        }
        else return "invalid operation";
    }
    
    private String executeRead(String[] tokens) {
        if (tokens[1].equalsIgnoreCase("baby")) {
            return readBaby(tokens);
        } else if (tokens[1].equalsIgnoreCase("compote")) {
            return readCompote(tokens);
        } else {
            return "unknown data";
        }
    }

    private String executeWrite(String[] tokens) {
        if (tokens[1].equalsIgnoreCase("baby")) {
            return writeBaby(tokens);
        } else if (tokens[1].equalsIgnoreCase("compote")) {
            return writeCompote(tokens);
        } else {
            return "unknown data";
        }

    }
    
    private String readBaby(String[] tokens) {
        try {
            if (tokens[2].equalsIgnoreCase("name")) {
                return service.loadByName(tokens[3], Baby.class).getObjects().toString();
            } else if (tokens[2].equalsIgnoreCase("id")) {
                long id = Long.parseLong(tokens[3]);
                return service.loadById(id, Baby.class).getObject().toString();
            } else {
                return "invalid data";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "invalid data";
        }
    }
    
    private String readCompote(String[]tokens){
                try {
            if (tokens[2].equalsIgnoreCase("name")) {
                return service.loadByName(tokens[3], Compote.class).getObjects().toString();
            } else if (tokens[2].equalsIgnoreCase("id")) {
                long id = Long.parseLong(tokens[3]);
                return service.loadById(id, Compote.class).getObject().toString();
            } else {
                return "invalid data";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "invalid data";
        }
    }

    private String readAny(String[] tokens) {
        return "is not supported";
    }

    private String writeBaby(String[] tokens) {
        try {
            int cellPhone = Integer.parseInt(tokens[3]);
            int homePhone = Integer.parseInt(tokens[4]);
            return service.save(new Baby(tokens[2], new Phone(cellPhone, homePhone))).toString();
        } catch (NumberFormatException ex) {
            return ex.getMessage();
        }
    }

    private String writeCompote(String[] tokens) {
        try {
            Set<Fruit>fruits=new HashSet<>();
            for (int i = 3; i < tokens.length; i+=2) {
                int fruitNumber = Integer.parseInt(tokens[i + 1]);
                fruits.add(new Fruit(tokens[i],fruitNumber));
            }
            return service.save(new Compote(tokens[2],fruits)).toString();
        } catch (NumberFormatException ex) {
            return ex.getMessage();
        }
    }

    private String writeAny(String[] tokens) {
        return "is not supported";
    }
}
