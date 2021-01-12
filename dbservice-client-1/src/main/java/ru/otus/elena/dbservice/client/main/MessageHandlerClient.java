
package ru.otus.elena.dbservice.client.main;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.otus.elena.dbservice.message.MessageContainer;

public class MessageHandlerClient {
    
    private static final Logger logger = Logger.getLogger(MessageHandlerClient.class.getName());
    private static final int WORKERS_COUNT = 2;
    private final BlockingQueue<MessageContainer> output = new LinkedBlockingQueue<>();
    public final static ArrayList<String>INPUT=new ArrayList<>();
    private Socket socket;
    private ExecutorService executor;
    
    protected MessageHandlerClient(Socket socket){
        this.socket=socket;
    }

    public void setSocket(Socket socket){
        this.socket=socket;
    }

    public void init() {
        executor = Executors.newFixedThreadPool(WORKERS_COUNT);
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }    

    public ArrayList<String> getInput() {
        return INPUT;
    }

    public void send(MessageContainer message) {
            output.add(message);        
    }
    
    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                MessageContainer msg = output.take();
                System.out.println("client sendMessage"+msg.getMessage());//blocks
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
            while ((inputLine = in.readLine()) != null) { //blocks
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
        }    
    }

    private void sortMessage(MessageContainer msg) {
        if (msg.getMessage().getMessage().equalsIgnoreCase("shutdown")) {
            ClientSocket.getClientSocket().close();
        } else {
            INPUT.add(msg.getMessage().toString());
        }
    }

    protected void close() {
        INPUT.add("connection is closed");
        if (executor != null) {
            executor.shutdown();
        }
        executor = null;

    }

}

