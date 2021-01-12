


package ru.otus.elena.dbservice.servlets.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.elena.dbservice.dbservice.DBService;
import ru.otus.elena.dbservice.services.TemplateProcessor;
import ru.otus.elena.dbservice.services.ServiceSetting;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.dbservice.main.DBServiceContext;

public class TableServlet extends HttpServlet{
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";   
    private static final String CREATE_PAGE_TEMPLATE = "createtable.html";
    private static final String DELETE_PAGE_TEMPLATE = "deletetable.html";
    private static final String ACTION_PAGE_TEMPLATE = "adminactions.html";
    //@Autowired
    private TemplateProcessor templateProcessor = DBServiceContext.getTemplateProcessor();
    //@Autowired
    private ServiceSetting serviceSetting = DBServiceContext.getServiceSetting();
    //@Autowired
    private DBService service = DBServiceContext.getService();

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String login = serviceSetting.getLogin();
        if (login == null) {
            pageVariables.put("result", "");
            page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
        } else {
            String action = request.getParameter("action");
            switch (action) {
                case "create":
                    String ctable = request.getParameter("table");                    
                    if (service != null) {
                        ArrayList<String> messages = service.createTable(ctable);
                        pageVariables.put("result", messages.toString());
                        page = templateProcessor.getPage(CREATE_PAGE_TEMPLATE, pageVariables);
                    } else {
                        pageVariables.put("result", "table " + ctable + " service doesn't exist");
                        page = templateProcessor.getPage(CREATE_PAGE_TEMPLATE, pageVariables);
                    }
                    break;
                case "delete":
                    String dtable = request.getParameter("table");
                    if (service != null) {
                        boolean isDeleted = false;
                        if (dtable.equalsIgnoreCase("all")) {
                            isDeleted = service.deleteAllTables();
                        } else {
                            isDeleted = service.deleteTable(dtable);
                        }
                        if (isDeleted) {
                            pageVariables.put("result", "table: " + dtable + " has deleted");
                            page = templateProcessor.getPage(DELETE_PAGE_TEMPLATE, pageVariables);
                        } else {
                            pageVariables.put("result", "table: " + dtable + " has not  deleted");
                            page = templateProcessor.getPage(DELETE_PAGE_TEMPLATE, pageVariables);
                        }
                    } else {
                        pageVariables.put("result", "table " + dtable + " service doesn't exist");
                        page = templateProcessor.getPage(DELETE_PAGE_TEMPLATE, pageVariables);
                    }
                    break;
                default:
                    pageVariables.put("result", "");
                    page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
            }
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String login = serviceSetting.getLogin();
        if (login == null) {
            pageVariables.put("result", "");
            page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
        } else {
            pageVariables.put("result", "");
            page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
