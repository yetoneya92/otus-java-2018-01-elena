package ru.otus.elena.dbservice.main;

import ru.otus.elena.dbservice.servlets.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.elena.dbservice.configuration.ServiceConfiguration;
import ru.otus.elena.dbservice.services.TemplateProcessor;

/**
 * mysql> CREATE USER 'me'@'localhost' IDENTIFIED BY 'me';
 * mysql> GRANT ALL PRIVILEGES ON * . * TO 'me'@'localhost';
 * mysql> create database db_example;
 * mysql> SET GLOBAL time_zone = '+3:00';
 * admin login="login", admin password = "password"
 */

public class DBServiceMain {
    
    public static final String ADDRESS = "service-1";
    private final static int PORT = 8091;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {
        DBServiceContext serviceContext=new DBServiceContext();
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
       // TemplateProcessor templateProcessor = new TemplateProcessor();
        context.addServlet(PreferenceServlet.class, "/preference");
        context.addServlet(AdminServlet.class, "/admin");
        context.addServlet(ActionServlet.class, "/action");
        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(CacheServlet.class, "/cache");        
        context.addServlet(TableServlet.class,"/table");
        context.addServlet(TestServlet.class,"/test");
        context.addServlet(MessageServlet.class,"/message");
        context.addServlet(ClearServlet.class,"/clear");
        context.addServlet(ConnectionServlet.class, "/connection");
        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));
        server.start();
        server.join();
    }
}
