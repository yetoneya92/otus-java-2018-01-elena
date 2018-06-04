
package ru.otus.elena.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.servlet.services.DBReader;
import ru.otus.elena.servlet.services.TemplateProcessor;
import ru.otus.elena.dbservice.interfaces.Service;

public class UserReadServlet extends HttpServlet{
            

    private static final String USERREAD_PAGE_TEMPLATE = "userread.html";
    private static final String USER_PAGE_TEMPLATE = "user.html";
    private static final String ERRORDATA_PAGE_TEMPLATE = "errordata.html";
    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public UserReadServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public UserReadServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
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
                    DBReader reader = DBReader.getReader();
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
                    DBReader reader = DBReader.getReader();
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
