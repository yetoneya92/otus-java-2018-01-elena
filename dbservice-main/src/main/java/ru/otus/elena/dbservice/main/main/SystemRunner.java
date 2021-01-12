package ru.otus.elena.dbservice.main.main;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.otus.elena.dbservice.message.Message;
import ru.otus.elena.dbservice.message.MessageContainer;


public class SystemRunner {

    private static final Logger logger = Logger.getLogger(SystemRunner.class.getName());
    private static final String MESSAGE_SYSTEM_START_COMMAND = "java -jar ../dbservice-msystem/target/msystem.jar";
    private static final String CLIENT_1_START_COMMAND = "java -jar ../dbservice-client-1/target/client.jar";
    private static final String CLIENT_2_START_COMMAND = "java -jar ../dbservice-client-2/target/client.jar";
    private static final String DBSERVICE_1_START_COMMAND = "java -jar ../dbservice-service-1/target/service.jar";
    private static final String DBSERVICE_2_START_COMMAND = "java -jar ../dbservice-service-2/target/service.jar";
    private static final int DB_SERVICE_START_DELAY_SEC = 2;
    private static final int CLIENT_START_DELAY_SEC = 4;
    private static ScheduledExecutorService executorService;
    private ArrayList<ProcessRunner> processRunners;
    private static ArrayList<String> messageList;
    private static final String HOST = "localhost";
    private static final int PORT = 5050;


    public void start() throws Exception {
        executorService = Executors.newScheduledThreadPool(5);
        processRunners = new ArrayList<>();
        messageList=new ArrayList<>();
        executorService.schedule(() -> {
            try {
                ProcessRunner systemRunner = new ProcessRunner("ms");
                systemRunner.start(MESSAGE_SYSTEM_START_COMMAND);
                processRunners.add(systemRunner);
                messageList.add("message system has started");
            } catch (IOException e) {
                messageList.add("message system has not started:"+e.getMessage());
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, 0, TimeUnit.SECONDS);
        executorService.schedule(() -> {
            try {
                ProcessRunner service_1_Runner=new ProcessRunner("s1");                
                service_1_Runner.start(DBSERVICE_1_START_COMMAND);
                processRunners.add(service_1_Runner);
                messageList.add("service_1 has started");
            } catch (IOException e) {
                messageList.add("service_1 has not started:"+e.getMessage());
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, DB_SERVICE_START_DELAY_SEC, TimeUnit.SECONDS);
        executorService.schedule(() -> {
            try {
                ProcessRunner service_2_Runner=new ProcessRunner("s2");
                service_2_Runner.start(DBSERVICE_2_START_COMMAND);
                processRunners.add(service_2_Runner);
                messageList.add("service_2 har started");
            } catch (IOException e) {
                messageList.add("service_2 has not started:"+e.getMessage());
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, DB_SERVICE_START_DELAY_SEC, TimeUnit.SECONDS);
        executorService.schedule(() -> {
            try {
                ProcessRunner client_1_Runner=new ProcessRunner("c1");
                client_1_Runner.start(CLIENT_1_START_COMMAND);
                processRunners.add(client_1_Runner);
                messageList.add("client_1 has started");
            } catch (IOException e) {
                messageList.add("client_1 has not started"+e.getMessage());
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);
        executorService.schedule(() -> {
            try {
                ProcessRunner client_2_Runner=new ProcessRunner("c2");
                client_2_Runner.start(CLIENT_2_START_COMMAND);
                processRunners.add(client_2_Runner);
                messageList.add("client_2 has started");
            } catch (IOException e) {
                messageList.add("client_2 has not started"+e.getMessage());
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);
    }

    public void shutdown() {
        try {
            Socket socket = new Socket(HOST, PORT);
            sendMessage(socket, new MessageContainer(new Message("shutdown"), null, "ms"));            
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
            socket.close();
            for (ProcessRunner runner : processRunners) {
                runner.stop();
            }
            executorService.shutdown();
        } catch (IOException ex) {
            messageList.add(ex.getMessage());
            logger.log(Level.SEVERE, ex.getMessage());
        }
    }
    
    protected void sendMessage(Socket socket, MessageContainer message) {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {                 
                String json = new Gson().toJson(message);
                out.println(json);
                out.println();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public ArrayList<String> getMessageList() {
        messageList.clear();
        for (ProcessRunner runner : processRunners) {
            messageList.add(runner.getOutput());
        }
        return messageList;
    }

    public void clearMessages() {
        for (ProcessRunner runner : processRunners) {
            runner.clearOutput();
        }
    }
}
