
package ru.otus.elena.messages.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
public class MessageSystem {

    private static volatile MessageSystem messageSystem = null;
    private static Map<User, ConcurrentLinkedQueue<Message>> messageMap;
    private static Map<User,ArrayList<String>>answerMap;
    private static List<Thread> workers;

    private MessageSystem() {
    }

    public static MessageSystem getMessageSystem() {
        if (messageSystem == null) {
            synchronized (MessageSystem.class) {
                if (messageSystem == null) {
                    answerMap=new HashMap<>();
                    workers=new ArrayList<>();
                    messageMap=new HashMap<>();
                    messageSystem = new MessageSystem();
                }
            }
        }
        return messageSystem;
    }

       public static void addUser(User user) {
        answerMap.put(user, new ArrayList<String>());
        messageMap.put(user, new ConcurrentLinkedQueue<Message>());
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                ConcurrentLinkedQueue<Message> queue =messageMap.get(user);
                if(!queue.isEmpty()){
                    Message message=queue.poll();
                    message.exec();
                    try{
                       Thread.sleep(10);
                    }catch(InterruptedException ex){
                        //Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        });
        thread.setName(user.toString());
        thread.setDaemon(true);
        thread.start();
        workers.add(thread);
    }

    public static void removeUser(User user) {
        for (Thread t : workers) {
            if (t.getName().equalsIgnoreCase(user.toString())) {
                t.interrupt();
            }
        }
    }

    public boolean sendMessage(Message message) {
        ConcurrentLinkedQueue<Message> queue = messageMap.getOrDefault(message.getUser(), null);
        if (queue == null) {
            return false;
        } else {
            queue.add(message);
        }
        return true;
    }
    public Map<User, ArrayList<String>> getAnswerMap(){
        return answerMap;
    }
    
    public ArrayList<String> getAnswer(User user) {
        return answerMap.getOrDefault(user, null);
    }

    public void dispose() {
        workers.forEach(Thread::interrupt);
    }

}
