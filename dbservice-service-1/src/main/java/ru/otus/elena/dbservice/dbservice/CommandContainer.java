
package ru.otus.elena.dbservice.dbservice;

import java.util.ArrayList;
import ru.otus.elena.dbservice.dataset.base.DataSet;

public class CommandContainer<T extends DataSet> {
    T object;
    String command;
    ArrayList<CommandContainer>generic;

    public CommandContainer(T object, String command, ArrayList<CommandContainer> generic) {
        this.object = object;
        this.command = command;
        this.generic = generic;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<CommandContainer> getGeneric() {
        return generic;
    }

    public T getObject() {
        return object;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    
    @Override
    public String toString() {
        return object.toString()+" "+command+" "+generic.toString();
    }
    
}
