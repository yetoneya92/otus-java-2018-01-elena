package ru.otus.elena.dbservice.client.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import ru.otus.elena.dbservice.client.services.TemplateProcessor;
import ru.otus.elena.dbservice.client.servlet.MessageServlet;
import ru.otus.elena.dbservice.client.servlet.ClearServlet;
import ru.otus.elena.dbservice.client.servlet.StartServlet;
import ru.otus.elena.dbservice.client.servlet.ActionServlet;
import ru.otus.elena.dbservice.client.servlet.ConnectionServlet;
import ru.otus.elena.dbservice.client.servlet.ServiceServlet;
import ru.otus.elena.dbservice.client.servlet.DataServlet;

import ru.otus.elena.dbservice.client.servlet.ReadServlet;
import ru.otus.elena.dbservice.client.servlet.WriteServlet;

public class ClientMain {
    public final static String ADDRESS="client-1";
    private final static int PORT = 8093;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {
        
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();
        context.addServlet(ConnectionServlet.class, "/connection");
        context.addServlet(StartServlet.class, "/start");
        context.addServlet(ServiceServlet.class, "/service");
        context.addServlet(ActionServlet.class, "/action");          
        context.addServlet(WriteServlet.class, "/write");
        context.addServlet(DataServlet.class, "/data");
        context.addServlet(ReadServlet.class, "/read");
        context.addServlet(MessageServlet.class, "/message");
        context.addServlet(ClearServlet.class, "/clear");
        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));
        server.start();
        server.join();
    }
}
