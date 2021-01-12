package ru.otus.elena.dbservice.msystem;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {

    private static volatile ServerMain serverMain = null;
    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());
    private static final int THREADS_NUMBER = 1;
    private static final int PORT = 5050;
    private static List<MessageHandler> clients;
    private boolean stop;

    private ServerMain() {
    }

    public static ServerMain getServerMain() {
        if (serverMain == null) {
            synchronized (ServerMain.class) {
                if (serverMain == null) {                    
                    clients = new CopyOnWriteArrayList<>();;
                    serverMain = new ServerMain();
                }
            }
        }
        return serverMain;
    }
    public static void main(String[] args) {
        System.out.println("ServerMain");
        try {
            getServerMain().start();
        } catch (IOException ex) {
             logger.log(Level.SEVERE, ex.toString());;
        }
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());
            while (!stop) {
                Socket socket = serverSocket.accept();
                MessageHandler handler = new MessageHandler(socket);
                handler.init();
                handler.addShutdownRegistration(() -> clients.remove(handler));
                clients.add(handler);
            }
            serverSocket.close();
        }
    }
    public void shutdown(){
        stop=true;
    }

    protected static List<MessageHandler> getClients() {
        return clients;
    }

}
