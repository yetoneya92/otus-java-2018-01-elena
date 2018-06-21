
package ru.otus.elena.dbservice.main;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.otus.elena.dbservice.message.Message;
import ru.otus.elena.dbservice.message.MessageContainer;

public class DBServiceSocket {
    
    private static final Logger logger = Logger.getLogger(DBServiceSocket.class.getName());
    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private static Socket socket;
    private static DBServiceSocket serviceSocket;
    private MessageHandlerService handler;

    private DBServiceSocket() {
    }

    public static DBServiceSocket getServiceSocket() {
        if (serviceSocket == null) {
            synchronized (DBServiceSocket.class) {
                if (serviceSocket == null) {
                    try {
                        socket = new Socket(HOST, PORT);
                        serviceSocket=new DBServiceSocket();
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, ex.getMessage());

                    }
                }
            }
        }
        return serviceSocket;
    }

    public void start() {
        handler=MessageHandlerService.getMessageHandlerService();
        handler.setSocket(socket);
        handler.init();
        handler.send(new MessageContainer(new Message(DBServiceMain.ADDRESS),DBServiceMain.ADDRESS,"ms"));
    }

}
