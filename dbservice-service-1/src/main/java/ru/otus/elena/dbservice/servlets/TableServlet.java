


package ru.otus.elena.dbservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.services.TemplateProcessor;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.dbservice.main.DBServicePreference;

public class TableServlet extends HttpServlet{
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";   
    private static final String CREATE_PAGE_TEMPLATE = "createtable.html";
    private static final String DELETE_PAGE_TEMPLATE = "deletetable.html";
    private static final String ACTION_PAGE_TEMPLATE = "adminactions.html";
    private final TemplateProcessor templateProcessor;
    
    @SuppressWarnings("WeakerAccess")
    public TableServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;        
    }

    @SuppressWarnings("WeakerAccess")
    public TableServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String login = DBServicePreference.getDBServicePreference().getLogin();
        if (login == null) {
            pageVariables.put("result", "");
            page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
        } else {
            Service service = DBServicePreference.getDBServicePreference().getService();
            String action = request.getParameter("action");
            switch (action) {
                case "create":
                    String ctable = request.getParameter("table");                    
                    if (service != null) {
                        boolean isCreated = service.createTable(ctable);
                        if (isCreated) {
                            pageVariables.put("result", "table " + ctable + " has created");
                            page = templateProcessor.getPage(CREATE_PAGE_TEMPLATE, pageVariables);
                        } else {
                            pageVariables.put("result", "table " + ctable + " has not created or already exists");
                            page = templateProcessor.getPage(CREATE_PAGE_TEMPLATE, pageVariables);
                        }
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
        String login = DBServicePreference.getDBServicePreference().getLogin();
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
