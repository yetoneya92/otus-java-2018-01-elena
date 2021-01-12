package ru.otus.elena.dbservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.elena.dbservice.configuration.ServiceConfiguration;
import ru.otus.elena.dbservice.main.DBServiceContext;
import ru.otus.elena.dbservice.services.ServiceSetting;
import ru.otus.elena.dbservice.main.ServiceSocket;
import ru.otus.elena.dbservice.services.TemplateProcessor;


public class ConnectionServlet extends HttpServlet {

    private static final String ACTION_PAGE_TEMPLATE = "adminactions.html";
    private static final String CONNECTION_PAGE_TEMPLATE = "connection.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    //@Autowired
    private ServiceSocket serviceSocket=DBServiceContext.getServiceSocket();
    //@Autowired
    private TemplateProcessor templateProcessor = DBServiceContext.getTemplateProcessor();
    //@Autowired
    private ServiceSetting serviceSetting = DBServiceContext.getServiceSetting();

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("open")) {
            if (!serviceSetting.isStarted()) {
                serviceSocket.start();
                if (serviceSetting.isStarted()) {
                    pageVariables.put("result", "connection has been opened");
                } else {
                    pageVariables.put("result", "connection has been not opened");
                }
            } else {
                pageVariables.put("result", "already opened");
            }
            if (serviceSetting.getLogin() != null) {
                page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
            } else {
                page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
            }
        } else if (action.equalsIgnoreCase("close")) {
            if (serviceSetting.isStarted()) {                
                serviceSocket.close();
                serviceSetting.setStarted(false);
                pageVariables.put("result", "closed");                
            }else{
                pageVariables.put("result", "connection has not been opened"); 
            }
            page = templateProcessor.getPage(CONNECTION_PAGE_TEMPLATE, pageVariables);
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("result", "");
        String page = templateProcessor.getPage(CONNECTION_PAGE_TEMPLATE, pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
