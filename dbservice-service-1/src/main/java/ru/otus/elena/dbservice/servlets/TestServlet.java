
package ru.otus.elena.dbservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.elena.dbservice.dbservice.DBServiceImpl;
import ru.otus.elena.dbservice.servlets.context.DBServiceContext;
import ru.otus.elena.dbservice.servlets.context.ServiceTest;
import ru.otus.elena.dbservice.servlets.context.TemplateProcessor;
import ru.otus.elena.dbservice.servlets.context.ServiceSetting;

public class TestServlet extends HttpServlet {

    private static final String TEST1_PAGE_TEMPLATE = "test1.html";
    private static final String TEST2_PAGE_TEMPLATE = "test2.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String ACTION_PAGE_TEMPLATE = "adminactions.html";
    private final TemplateProcessor templateProcessor=DBServiceContext.getTemplateProcessor();
    private final ServiceSetting serviceSetting=DBServiceContext.getServiceSetting();
    private final DBServiceImpl dBService=DBServiceContext.getdBService();
    private final ServiceTest serviceTest=DBServiceContext.getServiceTest();

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String page = null;
        Map<String, Object> pageVariables = new HashMap<>();        
        try {
            String action = request.getParameter("action");           
            switch (action) {
                case "launch":
                    if (serviceSetting.isTesting()) {
                        pageVariables.put("result", "test has been alredy launched");
                        page = templateProcessor.getPage(TEST2_PAGE_TEMPLATE, pageVariables);
                    } else {
                        if (dBService != null) {
                            serviceSetting.setTesting(true);
                            serviceTest.testDB();
                            pageVariables.put("result", "test has been launched");
                            page = templateProcessor.getPage(TEST2_PAGE_TEMPLATE, pageVariables);
                        } else {
                            pageVariables.put("result", "can not be launched, service doesn't exists");
                            page = templateProcessor.getPage(TEST1_PAGE_TEMPLATE, pageVariables);
                        }
                    }
                    break;
                case "stop":
                    if (serviceSetting.isTesting()) {
                        serviceTest.stopTest();
                        serviceSetting.setTesting(false);
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
