
package ru.otus.elena.dbservice.servlets.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.services.TemplateProcessor;




public class ClientServlet extends HttpServlet {

    private static final String READ_PAGE_TEMPLATE = "read.html";
    private static final String WRITE_PAGE_TEMPLATE = "write.html";
    private static final String ACTION_PAGE_TEMPLATE = "clientaction.html";

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public ClientServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public ClientServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("write")) {
            page = templateProcessor.getPage(WRITE_PAGE_TEMPLATE, pageVariables);
        } else if (action.equalsIgnoreCase("read")) {
            page = templateProcessor.getPage(READ_PAGE_TEMPLATE, pageVariables);
        } else {
            page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
   
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("result","");
        String page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    
}


