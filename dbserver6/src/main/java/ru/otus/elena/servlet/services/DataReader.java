
package ru.otus.elena.servlet.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dataset.tables.TableToWrite;
import ru.otus.elena.servlets.LoginServlet;
import ru.otus.elena.dbservice.interfaces.Service;

public class DataReader <T extends DataSet> {
    
    private Service service;
    public DataReader() {

    }

    public boolean setService(Service service, String login) {
        JSONParser parser = new JSONParser();
        try (InputStream ins = LoginServlet.class.getResourceAsStream("/json/password.json");
                InputStreamReader isr = new InputStreamReader(ins);) {
            JSONObject object = (JSONObject) parser.parse(isr);
            if (login.equalsIgnoreCase((String) object.get("login"))) {
                this.service = service;
                return true;
            } else {
                return false;
            }
        } catch (IOException | ParseException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public Object readById(String tableName, long id) {
        try {
            TableToWrite table = TableToWrite.valueOf(tableName);
            Class<T> clazz = table.getClazz();
            Object obj = service.loadById(id, clazz);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }


    public Object readByName(String tableName,String name) {
        try {
            TableToWrite table = TableToWrite.valueOf(tableName);
            Class<T> clazz = table.getClazz();
            Object obj = service.loadByName(name, clazz);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }
}
