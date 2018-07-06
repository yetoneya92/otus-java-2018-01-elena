
package ru.otus.elena.dbservice.websocket;

import java.io.IOException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ru.otus.elena.dbservice.main.DBServiceContext;



    @WebSocket
    public class MySocket {
        
         private MessageExecutor messageExecutor=DBServiceContext.getMessageExecutor();
         private Session session;

        @OnWebSocketConnect
        public void onConnect(Session session) {
            System.out.println("Connect: " + session.getRemoteAddress().getAddress());
            try {
                this.session = session;
                session.getRemote().sendString("Got your connect message");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     
    @OnWebSocketMessage
    public void onText(String message) {        
        System.out.println("text: " + message);
        String result=messageExecutor.execute(message);
        try {
            this.session.getRemote().sendString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);       
    }
}