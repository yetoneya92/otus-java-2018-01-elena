
package ru.otus.elena.dbservice.dbservice;

import java.util.ArrayList;
import ru.otus.elena.dbservice.dataset.base.DataSet;


public class LoadResult<T extends DataSet> {
    private T object;
    private ArrayList<T> objects;
    private ArrayList<String> messages;

    public LoadResult(ArrayList<T> objects, ArrayList<String> messages) {
        this.objects = objects;
        this.messages = messages;
    }

    public LoadResult(T object, ArrayList<String> messages) {
        this.object = object;
        this.messages = messages;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public T getObject() {
        return object;
    }
    
    public ArrayList<T> getObjects() {
        return objects;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!messages.isEmpty()) {
            builder.append(messages.toString());
        }
        if (object != null) {
            builder.append(object.toString()).append("\n");
        }
        else if (objects != null && !objects.isEmpty()) {
            builder.append(objects.toString()).append("\n");
        }

        return builder.toString();
    }

   
    
}
