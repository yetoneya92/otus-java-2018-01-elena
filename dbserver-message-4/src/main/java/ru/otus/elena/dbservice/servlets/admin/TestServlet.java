
package ru.otus.elena.dbservice.servlets.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.dbservice.DBService;
import ru.otus.elena.dbservice.services.ServiceTest;
import ru.otus.elena.dbservice.services.TemplateProcessor;
import ru.otus.elena.dbservice.services.ServiceSetting;
import ru.otus.elena.dbservice.main.DBServiceContext;

public class TestServlet extends HttpServlet {

    private static final String TEST1_PAGE_TEMPLATE = "test1.html";
    private static final String TEST2_PAGE_TEMPLATE = "test2.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String ACTION_PAGE_TEMPLATE = "adminactions.html";
    //@Autowired
    private TemplateProcessor templateProcessor = DBServiceContext.getTemplateProcessor();
    //@Autowired
    private ServiceSetting serviceSetting = DBServiceContext.getServiceSetting();
    //@Autowired
    private DBService service = DBServiceContext.getService();
    //Autowired
    private ServiceTest serviceTest=DBServiceContext.getServiceTest();

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
                        if (service != null) {
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
