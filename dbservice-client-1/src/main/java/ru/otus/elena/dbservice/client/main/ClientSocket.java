
package ru.otus.elena.dbservice.client.main;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.otus.elena.dbservice.message.Message;
import ru.otus.elena.dbservice.message.MessageContainer;

public class ClientSocket {

    private static final Logger logger = Logger.getLogger(ClientSocket.class.getName());
    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private static Socket socket;
    private static ClientSocket clientSocket;
    private static MessageHandlerClient handler;
    private static String serviceAddress;

    private ClientSocket(Socket socket) {
        this.socket=socket;
    }

    public static ClientSocket getClientSocket() {
        if (clientSocket == null) {
            synchronized (ClientSocket.class) {
                if (clientSocket == null) {
                    try {
                        while (true) {
                            socket = new Socket(HOST, PORT);
                            if (socket != null) {
                                clientSocket = new ClientSocket(socket);
                                break;
                            }
                        }                            
                }catch (IOException ex) {
                        logger.log(Level.SEVERE, ex.getMessage());

                    }
                }
            }
        }
        return clientSocket;
    }

    public MessageHandlerClient getHandler() {
        return handler;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }


    public void start() {
        handler = new MessageHandlerClient(socket);
        handler.init();
        handler.send(new MessageContainer(new Message(ClientMain.ADDRESS), ClientMain.ADDRESS, "ms"));
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (handler != null) {
                handler.close();
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
        clientSocket = null;
        socket = null;
        handler = null;
    }
}
