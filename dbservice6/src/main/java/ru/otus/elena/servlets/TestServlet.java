
package ru.otus.elena.servlets;

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
import ru.otus.elena.servlet.services.ServiceTest;
import ru.otus.elena.servlet.services.TemplateProcessor;
import ru.otus.elena.dbservice.interfaces.Service;

public class TestServlet extends HttpServlet{
    
    private static final String TEST1_PAGE_TEMPLATE = "test1.html";
    private static final String TEST2_PAGE_TEMPLATE = "test2.html";
    private static final String ACTION_PAGE_TEMPLATE="adminaction.html";
    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
                ApplicationContext context = (ApplicationContext) request.getSession().getAttribute("context");
        if (context == null) {
            context = new AnnotationConfigApplicationContext(ServiceConfiguration.class);
            request.getSession().setAttribute("context", context);
        }
        templateProcessor=context.getBean(TemplateProcessor.class);
        String page = null;
        Map<String, Object> pageVariables = new HashMap<>();

        try {
            ServiceTest serviceTest;
            String action = request.getParameter("action");           
            switch (action) {
                case "launch":
                    serviceTest = (ServiceTest) request.getSession().getAttribute("dbtest");
                    if (serviceTest != null) {
                        serviceTest.stopTest();
                    }
                    Service service = (Service) request.getSession().getAttribute("service");
                    if (service != null) {
                        context = (ApplicationContext) request.getSession().getAttribute("context");
                        if (context != null) {
                            if (serviceTest == null) {
                                serviceTest = context.getBean(ServiceTest.class);
                            }
                            //serviceTest.setService(service);
                            request.getSession().setAttribute("dbtest", serviceTest);
                            serviceTest.testDB();
                            pageVariables.put("result", "test has been launched");
                            page = templateProcessor.getPage(TEST2_PAGE_TEMPLATE, pageVariables);
                        } else {
                            pageVariables.put("result", "can not be launched, set service");
                            page = templateProcessor.getPage(TEST1_PAGE_TEMPLATE, pageVariables);
                        }
                    } else {
                        pageVariables.put("result", "can not be launched, set service");
                        page = templateProcessor.getPage(TEST1_PAGE_TEMPLATE, pageVariables);
                    }
                    break;
                case "stop":
                    serviceTest = (ServiceTest) request.getSession().getAttribute("dbtest");
                    if (serviceTest != null) {
                        serviceTest.stopTest();
                        request.getSession().setAttribute("dbtest", null);
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
}
