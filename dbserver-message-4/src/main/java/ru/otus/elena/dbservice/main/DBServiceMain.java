package ru.otus.elena.dbservice.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import ru.otus.elena.dbservice.services.TemplateProcessor;
import ru.otus.elena.dbservice.servlets.admin.ActionServlet;
import ru.otus.elena.dbservice.servlets.admin.AdminServlet;
import ru.otus.elena.dbservice.servlets.admin.CacheServlet;
import ru.otus.elena.dbservice.servlets.admin.LoginServlet;
import ru.otus.elena.dbservice.servlets.admin.PreferenceServlet;
import ru.otus.elena.dbservice.servlets.admin.TableServlet;
import ru.otus.elena.dbservice.servlets.admin.TestServlet;
import ru.otus.elena.dbservice.servlets.client.ClientServlet;
import ru.otus.elena.dbservice.servlets.client.WriteServlet;
import ru.otus.elena.dbservice.websocket.SocketHandler;

/**
 * mysql> CREATE USER 'me'@'localhost' IDENTIFIED BY 'me';
 * mysql> GRANT ALL PRIVILEGES ON * . * TO 'me'@'localhost';
 * mysql> create database db_example;
 * mysql> SET GLOBAL time_zone = '+3:00';
 * admin login="login", admin password = "password"
 */

public class DBServiceMain {
    
    private final static int PORT = 8091;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {
        DBServiceContext serviceContext=new DBServiceContext();
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();
        context.addServlet(PreferenceServlet.class, "/preference");
        context.addServlet(AdminServlet.class, "/admin");
        context.addServlet(ActionServlet.class, "/action");
        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(CacheServlet.class, "/cache");        
        context.addServlet(TableServlet.class, "/table");
        context.addServlet(TestServlet.class, "/test");
        context.addServlet(ClientServlet.class, "/client");
        context.addServlet(WriteServlet.class, "/write");
        Server server = new Server(PORT);
        server.setHandler(new HandlerList(new SocketHandler(),resourceHandler, context));
        server.start();
        server.join();
    }
}
