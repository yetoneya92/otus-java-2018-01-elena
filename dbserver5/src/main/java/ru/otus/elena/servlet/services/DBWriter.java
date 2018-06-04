
package ru.otus.elena.servlet.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.servlets.LoginServlet;
import ru.otus.elena.dbservice.interfaces.Service;

public class DBWriter {

    private static DBWriter writer = null;

    private Service service;

    private DBWriter() {
    }


    public <T extends DataSet> boolean writeObject(T object) {
        if (service == null) {
            return false;
        }
        int saved=service.save(object);
        return saved!=0;
    }

    public boolean setService(Service service, String login) {
        JSONParser parser = new JSONParser();
        try (InputStream ins = LoginServlet.class.getResourceAsStream("/json/password.json");
                InputStreamReader reader = new InputStreamReader(ins);) {
            JSONObject object = (JSONObject) parser.parse(reader);
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

    public static synchronized DBWriter getWriter() {
        if (writer == null) {
            writer = new DBWriter();
        }
        return writer;
    }
}
