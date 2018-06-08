
package ru.otus.elena.servlet.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.servlet.servlets.LoginServlet;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.messages.message.User;

public class DBWriter {

    private static DBWriter writer = null;

    private Service service;

    private DBWriter() {
    }

 
    public <T extends DataSet> ArrayList<String> writeObject(T object) {
        if (service == null) {
            return null;
        }
        ArrayList<String>messages=service.save(object);
        return messages;
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
            synchronized (DBWriter.class) {
                if (writer == null) {
                    writer = new DBWriter();
                }
            }
        }
        return writer;
    }
}
