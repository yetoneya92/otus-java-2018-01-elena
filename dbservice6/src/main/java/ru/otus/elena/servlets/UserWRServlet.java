
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
import ru.otus.elena.servlet.services.TemplateProcessor;
import ru.otus.elena.dbservice.interfaces.Service;

public class UserWRServlet extends HttpServlet {

    private static final String READ_PAGE_TEMPLATE = "userread.html";
    private static final String WRITE_PAGE_TEMPLATE = "userwrite.html";
    private static final String USER_PAGE_TEMPLATE = "user.html";
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
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        Service service = (Service) request.getSession().getAttribute("service");
        if (service == null) {
            pageVariables.put("result", "service doesn't exists");
            page = templateProcessor.getPage(USER_PAGE_TEMPLATE, pageVariables);
        } else {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase("write")) {
                pageVariables.put("result", "");
                page = templateProcessor.getPage(WRITE_PAGE_TEMPLATE, pageVariables);
            } else if (action.equalsIgnoreCase("read")) {
                pageVariables.put("result", "");
                page = templateProcessor.getPage(READ_PAGE_TEMPLATE, pageVariables);
            } else {
                pageVariables.put("result", "unknown action");
                page = templateProcessor.getPage(USER_PAGE_TEMPLATE, pageVariables);
            }
        }
        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}


