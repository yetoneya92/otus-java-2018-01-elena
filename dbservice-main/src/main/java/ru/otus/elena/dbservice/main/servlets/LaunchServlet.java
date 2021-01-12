
package ru.otus.elena.dbservice.main.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LaunchServlet extends HttpServlet{
    
    private static final String ACTIONS_PAGE_TEMPLATE = "action.html";    

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public LaunchServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public LaunchServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put("result", "");
        String page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
