
package ru.otus.elena.dbserver;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;

public class JsonWriter {

    private static final String FILENAME = "public_html/cache/cachedata.json";

    public void writeJson(int size, int hit, int miss) {

        try (FileWriter fw = new FileWriter(FILENAME)) {

            JSONObject object = new JSONObject();
            object.put("size", size);
            object.put("hit", hit);
            object.put("miss", miss);
            fw.write(object.toJSONString());
            fw.flush();

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());

        }
    }
}
