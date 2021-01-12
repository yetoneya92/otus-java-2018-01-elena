package ru.otus.elena.dbservice.message;

public class MessageContainer {
    private String from;
    private String to;
    Message  message;
    public MessageContainer(Message message,String from,String to){
        this.message=message;
        this.from=from;
        this.to=to;
    }

    public Message getMessage() {
        return message;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }
    
}
