package ru.otus.elena.dbservice;

import com.mysql.cj.jdbc.MysqlDataSource;
import ru.otus.elena.dbservice.servlets.ClearServlet;
import ru.otus.elena.dbservice.servlets.PreferenceServlet;
import ru.otus.elena.dbservice.servlets.TestServlet;
import ru.otus.elena.dbservice.servlets.ConnectionServlet;
import ru.otus.elena.dbservice.servlets.CacheServlet;
import ru.otus.elena.dbservice.servlets.LoginServlet;
import ru.otus.elena.dbservice.servlets.ActionServlet;
import ru.otus.elena.dbservice.servlets.AdminServlet;
import ru.otus.elena.dbservice.servlets.MessageServlet;
import ru.otus.elena.dbservice.servlets.TableServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.elena.dbservice.servlets.context.DBServiceContext;

/**
 * mysql> CREATE USER 'me'@'localhost' IDENTIFIED BY 'me';
 * mysql> GRANT ALL PRIVILEGES ON * . * TO 'me'@'localhost';
 * mysql> create database db_example;
 * mysql> SET GLOBAL time_zone = '+3:00';
 * admin login="login", admin password = "password"
 */


@Configuration
@ComponentScan
public class DBServiceMain {
    
    public static final String ADDRESS = "service-1";
    private final static int PORT = 8091;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext cntx = new AnnotationConfigApplicationContext(DBServiceMain.class);
        DBServiceContext dsc=new DBServiceContext(cntx);
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
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
    
    @Bean("source")
    public MysqlDataSource source() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setDatabaseName("db_example");
        ds.setUser("me");
        ds.setServerName("localhost");
        ds.setPassword("me");
        return ds;
    }

}
