package ru.otus.elena.dbservice.message;

import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;

public class Message<T> {

    private Baby baby;
    private Compote compote;
    private long id;
    private String tableName;
    private String name;
    private String message;

    public Message(String message) {
        this.message = message;
    }

    public Message(Baby baby) {
        this.baby = baby;
    }

    public Message(Compote compote) {
        this.compote = compote;
    }

    public Message(String tableName, long id) {
        this.id = id;
        this.tableName = tableName;
    }

    public Message(String tableName, String name) {
        this.name = name;
        this.tableName = tableName;
    }

    public String getName() {
        return name;
    }

    public Baby getBaby() {
        return baby;
    }

    public Compote getCompote() {
        return compote;
    }
    

    public long getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        if (baby != null) {
            builder.append(baby.toString());
        }
                
        if (compote != null) {
            builder.append(compote.toString());
        }

        if (message != null) {
            builder.append(message);
        }
        return builder.toString();
    }
}
