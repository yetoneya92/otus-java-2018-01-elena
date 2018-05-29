package ru.otus.elena.servlets;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import ru.otus.elena.servlet.services.TemplateProcessor;

/**
 * mysql> CREATE USER 'me'@'localhost' IDENTIFIED BY 'me';
 * mysql> GRANT ALL PRIVILEGES ON * . * TO 'me'@'localhost';
 * mysql> create database db_example;
 * mysql> SET GLOBAL time_zone = '+3:00';
 * admin login="login", admin password = "password"
 */

public class Main {
    private final static int PORT = 8091;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();
        context.addServlet(PreferenceServlet.class, "/preference");
        context.addServlet(AdminServlet.class, "/admin");
        context.addServlet(ActionServlet.class, "/action");
        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(CacheServlet.class, "/cache");
        context.addServlet(UserServlet.class, "/user");
        context.addServlet(UserWriteServlet.class, "/userwrite");
        context.addServlet(UserDataServlet.class,"/data");
        context.addServlet(UserWRServlet.class,"/wr");
        context.addServlet(UserReadServlet.class,"/userread");
        context.addServlet(TableServlet.class,"/table");
        context.addServlet(TestServlet.class,"/test");
        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));
        
        server.start();
        server.join();
    }
}
