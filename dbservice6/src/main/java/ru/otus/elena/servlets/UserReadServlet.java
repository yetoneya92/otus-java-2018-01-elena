
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
import ru.otus.elena.servlet.services.DataReader;
import ru.otus.elena.servlet.services.TemplateProcessor;
import ru.otus.elena.dbservice.interfaces.Service;

public class UserReadServlet extends HttpServlet{
            

    private static final String USERREAD_PAGE_TEMPLATE = "userread.html";
    private static final String USER_PAGE_TEMPLATE = "user.html";
    private static final String ERRORDATA_PAGE_TEMPLATE = "errordata.html";
    
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
        Service service = (Service) request.getSession().getAttribute("service");

        try {
            if (service == null) {
                pageVariables.put("result", "service doesn't exists");
                page = templateProcessor.getPage(USER_PAGE_TEMPLATE, pageVariables);
            } else {
                String table = request.getParameter("tablename");             //
                String _id = request.getParameter("id");
                String name = request.getParameter("name");
                if (!_id.equalsIgnoreCase("")) {
                    long id = Long.parseLong(_id);
                    DataReader reader = (DataReader) request.getSession().getAttribute("reader");
                    Object obj = reader.readById(table, id);
                    if (obj == null) {
                        pageVariables.put("result","object has not been read");
                        page = templateProcessor.getPage(USERREAD_PAGE_TEMPLATE, pageVariables);
                        
                    } else {
                        String str = obj.toString();
                        pageVariables.put("result", str);
                        page = templateProcessor.getPage(USERREAD_PAGE_TEMPLATE, pageVariables);
                    }
                } else if (!name.equalsIgnoreCase("")) {
                    DataReader reader = (DataReader) request.getSession().getAttribute("reader");
                    Object obj = reader.readByName(table, name);
                    if (obj != null) {
                        String str = obj.toString();
                        pageVariables.put("result", str);
                    } else {
                        pageVariables.put("result", "object has not been loaded");
                    }
                    page = templateProcessor.getPage(USERREAD_PAGE_TEMPLATE, pageVariables);
                } else {
                    page = templateProcessor.getPage(ERRORDATA_PAGE_TEMPLATE, pageVariables);
                }
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException ex) {
            page = templateProcessor.getPage(ERRORDATA_PAGE_TEMPLATE, pageVariables);
            System.out.println(ex.getMessage());
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
