
package ru.otus.elena.dbservice.main;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.dbservice.message.Message;
import ru.otus.elena.dbservice.message.MessageContainer;
import ru.otus.elena.dbservice.services.ServiceSetting;

public class ServiceSocket {
    
    private static final Logger logger = Logger.getLogger(ServiceSocket.class.getName());
    private final static String HOST = "localhost";
    private final static int PORT = 5050;
    private Socket socket;
    @Autowired
    private MessageHandlerService handler;
    @Autowired
    private ServiceSetting serviceSetting;
  
    public ServiceSocket() {
    }
    
    private void setSocket() throws IOException {
            while (true) {
                socket = new Socket(HOST, PORT);
                if (socket != null) {
                    break;
                
            }
        } 
    }

    public void start() {
        try {
            setSocket();
            handler.setSocket(socket);
            handler.init();
            handler.send(new MessageContainer(new Message(DBServiceMain.ADDRESS), DBServiceMain.ADDRESS, "ms"));
            serviceSetting.setStarted(true);
        } catch (IOException ex) {
           handler.getInputMessages().add(ex.getMessage());
           serviceSetting.setStarted(false);
        }
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
    }
}


