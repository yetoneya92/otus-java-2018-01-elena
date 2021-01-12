
package ru.otus.elena.dbservice.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.otus.elena.dbservice.message.MessageContainer;
import com.google.gson.Gson;
import java.util.ArrayList;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.dbservice.message.Message;

public class MessageHandlerService {

    private static final Logger logger = Logger.getLogger(MessageHandlerService.class.getName());
    private static final int WORKERS_COUNT = 3;
    private final BlockingQueue<MessageContainer> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<MessageContainer> input = new LinkedBlockingQueue<>();
    private final static ArrayList<String> inputMessageList=new ArrayList();
    private static MessageExecutor messageExecutor;
    private  Socket socket;
    private static ExecutorService executor;
    public static MessageHandlerService handler=null;
    private static Service service;
    private  volatile boolean isStopped=true;
 
    public static MessageHandlerService getMessageHandlerService() {
        if (handler == null) {
            synchronized(MessageHandlerService.class){
                if(handler==null){
                   executor = Executors.newFixedThreadPool(WORKERS_COUNT); 
                   messageExecutor=new MessageExecutor();
                   handler=new MessageHandlerService();
                }
            }
        }
        return handler;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setService(Service service) {
        this.service = service;
        isStopped=false;
        executor.execute(this::executeMessage);
        
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    protected BlockingQueue<MessageContainer> getOutput() {
        return output;
    }
    
    public ArrayList<String> getInputMessages() {
        return inputMessageList;
    }

    public void send(MessageContainer message) {
         output.add(message);
    }

    protected void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                MessageContainer msg = output.take(); 
                System.out.println("service send:"+msg.getMessage().toString());
                String json = new Gson().toJson(msg);
                out.println(json);
                out.println();
            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) {
                    String json = stringBuilder.toString();
                    MessageContainer msg = (MessageContainer) new Gson().fromJson(json, MessageContainer.class);
                    if (isStopped) {
                        output.add(new MessageContainer(new Message("service doesn't work"), msg.getTo(), msg.getFrom()));
                    } else {
                        input.add(msg);
                    }
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void executeMessage() {
        while (!isStopped) {
            try {
                MessageContainer inputMessage = input.take();
                inputMessageList.add(inputMessage.getMessage().toString());
                if (inputMessage.getFrom().equalsIgnoreCase("ms")) {
                    execMSMessage(inputMessage.getMessage());
                } else {
                    MessageContainer outputMessage = messageExecutor.exec(inputMessage);
                    output.add(outputMessage);
                }
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
            }
        }
    }
    
    private void execMSMessage(Message message){
        if(message.getMessage().equalsIgnoreCase("shutdown")){
            close();
        }
    }
    
    public void close() {
        if (!isStopped) {
            isStopped = true;
            try {
                socket.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
            }
            service.shutDown();
            executor.shutdown();            
        }
    }
    public void shutdownService(){
        isStopped=true;
        service.shutDown();
    }
}
