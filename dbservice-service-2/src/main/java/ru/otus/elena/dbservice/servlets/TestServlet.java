
package ru.otus.elena.dbservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.services.DBTest;
import ru.otus.elena.dbservice.services.TemplateProcessor;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.dbservice.main.DBServicePreference;

public class TestServlet extends HttpServlet {

    private static final String TEST1_PAGE_TEMPLATE = "test1.html";
    private static final String TEST2_PAGE_TEMPLATE = "test2.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String ACTION_PAGE_TEMPLATE = "adminactions.html";
    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public TestServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public TestServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String page = null;
        Map<String, Object> pageVariables = new HashMap<>();        
        try {
            DBTest dbtest=DBServicePreference.getDBServicePreference().getDBTest();
            String action = request.getParameter("action");           
            switch (action) {
                case "launch":
                    if (dbtest != null) {
                        dbtest.stopTest();
                    }
                    Service service = DBServicePreference.getDBServicePreference().getService();
                    if (service != null) {
                        dbtest = new DBTest(service);
                        DBServicePreference.getDBServicePreference().setDBTest(dbtest);
                        dbtest.testDB();
                        pageVariables.put("result", "test has been launched");
                        page = templateProcessor.getPage(TEST2_PAGE_TEMPLATE, pageVariables);
                    } else {
                        pageVariables.put("result", "can not be launched, set service");
                        page = templateProcessor.getPage(TEST1_PAGE_TEMPLATE, pageVariables);
                    }
                    break;
                case "stop":
                    if (dbtest != null) {
                        dbtest.stopTest();
                        DBServicePreference.getDBServicePreference().setDBTest(null);
                        pageVariables.put("result", "test has been stopped");
                        page = templateProcessor.getPage(TEST1_PAGE_TEMPLATE, pageVariables);
                    } else {
                        pageVariables.put("result", "test has not been launched");
                        page = templateProcessor.getPage(TEST1_PAGE_TEMPLATE, pageVariables);
                    }
                    break;

                default:
                    pageVariables.put("result", "unknown action");
                    page = templateProcessor.getPage(TEST1_PAGE_TEMPLATE, pageVariables);
            }

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            pageVariables.put("result", "can not be launched, set service");
            page = templateProcessor.getPage(TEST1_PAGE_TEMPLATE, pageVariables);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        }
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
