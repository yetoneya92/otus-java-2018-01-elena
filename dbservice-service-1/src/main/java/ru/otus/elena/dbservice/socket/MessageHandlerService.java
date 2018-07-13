
package ru.otus.elena.dbservice.socket;

import ru.otus.elena.dbservice.servlets.context.ServiceSetting;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.elena.dbservice.message.Message;
import ru.otus.elena.dbservice.interfaces.DBService;

@Service("handler")
public class MessageHandlerService {

    private static final Logger logger = Logger.getLogger(MessageHandlerService.class.getName());
    private static final int WORKERS_COUNT = 3;
    private final BlockingQueue<MessageContainer> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<MessageContainer> input = new LinkedBlockingQueue<>();
    private final ArrayList<String> inputMessageList=new ArrayList();
    @Autowired
    private MessageExecutor messageExecutor;
    private Socket socket;
    private boolean isStopped=true;
    private ExecutorService executor;
    @Autowired
    private DBService service;

    public MessageHandlerService(){
        
    }
    
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void init() {        
        executor=Executors.newFixedThreadPool(WORKERS_COUNT);
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
    
    public void startExec() {
        isStopped = false;
        executor.execute(this::executeMessage);
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

    private void execMSMessage(Message message) {
        if (message.getMessage().equalsIgnoreCase("shutdown")) {
            close();
        }
    }

    public void shutdownService() {
        isStopped=true;
        service.shutDown();
    }

    public void close() {
        shutdownService();
        if (executor != null) {
            executor.shutdown();
        }
        executor = null;
    }
}

