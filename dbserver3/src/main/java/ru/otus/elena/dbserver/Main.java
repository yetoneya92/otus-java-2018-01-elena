package ru.otus.elena.dbserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * mysql> CREATE USER 'me'@'localhost' IDENTIFIED BY 'me';
 * mysql> GRANT ALL PRIVILEGES ON * . * TO 'me'@'localhost';
 * mysql> create database db_example;
 * mysql> SET GLOBAL time_zone = '+3:00';
 * admin login="login", admin password = "password"
 */

public class Main {
    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();
        context.addServlet(UserServlet.class, "/userservlet");
        context.addServlet(AdminServlet.class, "/adminservlet");
        context.addServlet(CacheServlet.class, "/cacheservlet");
        context.addServlet(LoginServlet.class, "/loginservlet");
        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }
}
