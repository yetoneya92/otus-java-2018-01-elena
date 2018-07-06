
package ru.otus.elena.dbservice.servlets.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.services.TemplateProcessor;


public class WriteServlet extends HttpServlet {

    private static final String BABY_PAGE_TEMPLATE = "baby.html";
    private static final String COMPOTE_PAGE_TEMPLATE = "compote.html";
    private static final String WRITE_PAGE_TEMPLATE = "write.html";
    private static final String ACTION_PAGE_TEMPLATE = "clientaction.html";
    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public WriteServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public WriteServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
            String table = request.getParameter("tablename");
            if (table.equalsIgnoreCase("baby")) {
                pageVariables.put("result", "");
                page = templateProcessor.getPage(BABY_PAGE_TEMPLATE, pageVariables);
            } else if (table.equalsIgnoreCase("compote")) {
                pageVariables.put("result", "name has to be unicum");
                page = templateProcessor.getPage(COMPOTE_PAGE_TEMPLATE, pageVariables);
            } else {
                pageVariables.put("result", "table has not found");
                page = templateProcessor.getPage(WRITE_PAGE_TEMPLATE, pageVariables);
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
        String page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
