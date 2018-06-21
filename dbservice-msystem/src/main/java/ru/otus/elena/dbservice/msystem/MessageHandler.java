
package ru.otus.elena.dbservice.msystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import ru.otus.elena.dbservice.message.Message;
import ru.otus.elena.dbservice.message.MessageContainer;

public class MessageHandler {

    private static final Logger logger = Logger.getLogger(MessageHandler.class.getName());
    private static final int WORKERS_COUNT = 2;
    private final BlockingQueue<MessageContainer> output = new LinkedBlockingQueue<>();
    private String clientName;
    private final Socket socket;
    private final ExecutorService executor;
    private final List<Runnable> shutdownRegistrations;

    public MessageHandler(Socket socket) {
        this.socket = socket;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
        this.shutdownRegistrations = new ArrayList<>();
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    public void addShutdownRegistration(Runnable runnable) {
        this.shutdownRegistrations.add(runnable);
    }

    protected BlockingQueue<MessageContainer> getOutput() {
        return output;
    }


    public String getClientName() {
        return clientName;
    }

    protected void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                MessageContainer msg = output.take(); //blocks
                System.out.println("messageSystem sendMessage:"+msg.getMessage().toString());
                String json = new Gson().toJson(msg);
                out.println(json);
                out.println();//line with json + an empty line
            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {//blocks               

                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) { //empty lin e is the end of the message
                    String json = stringBuilder.toString();
                    MessageContainer msg = (MessageContainer) new Gson().fromJson(json, MessageContainer.class);
                    sortMessage(msg);
                    stringBuilder = new StringBuilder();
                }
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
    }


    private void sortMessage(MessageContainer msg) {
        if (clientName == null) {
            clientName = msg.getFrom();
            System.out.println("messageSystem:"+msg.getMessage().toString());
            output.add(new MessageContainer(new Message("registered:" + clientName), "ms", clientName));
        }
        String to = msg.getTo();
        if (to.equalsIgnoreCase("ms")) {
            execMSMessage(msg.getMessage());//
        } else if (to != null) {
            boolean[] isSend=new boolean[1];
            ServerMain.getClients().forEach(s -> {
                if (s.getClientName().equalsIgnoreCase(to)) {
                    s.getOutput().add(msg);
                    isSend[0]=true;
                }
            });
            if (isSend[0] == false) {
                this.getOutput().add(new MessageContainer(new Message("unknown receiver"), "ms", msg.getFrom()));
            }
        } else if (msg.getFrom() != null) {
            ServerMain.getClients().forEach(s -> {
                if (s.getClientName().equalsIgnoreCase(msg.getFrom())) {
                    s.getOutput().add(new MessageContainer(new Message("unknown receiver"), "ms", msg.getFrom()));
                }
            });
        } else {
        }
    }

    private void execMSMessage(Message message) {
        if (message.getMessage().equalsIgnoreCase("shutdown")) {
            ServerMain.getClients().forEach(s -> s.getOutput().add(new MessageContainer(new Message("shutdown"), "ms", s.getClientName())));
            ServerMain.getServerMain().shutdown();
        } 
    }


    public void close() {
        shutdownRegistrations.forEach(Runnable::run);
        shutdownRegistrations.clear();
        executor.shutdown();
    }

}
