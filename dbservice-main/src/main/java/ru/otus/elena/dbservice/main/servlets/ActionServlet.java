
package ru.otus.elena.dbservice.main.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.main.main.SystemRunner;

public class ActionServlet extends HttpServlet{
    
    private static final String ACTION_PAGE_TEMPLATE = "action.html";


    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public ActionServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public ActionServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("start")) {
            if (request.getSession().getAttribute("runner") == null) {
                SystemRunner runner = new SystemRunner();
                try {
                    runner.start();
                } catch (Exception ex) {
                    pageVariables.put("result", "system has not started: " + ex.getMessage());
                }
                request.getSession().setAttribute("runner", runner);
                pageVariables.put("result", "system has started");
            } else {
                pageVariables.put("result", "system already worked");
            }
        } else if (action.equalsIgnoreCase("shutdown")) {
            if (request.getSession().getAttribute("runner") != null) {
                SystemRunner runner = (SystemRunner) request.getSession().getAttribute("runner");
                request.getSession().setAttribute("runner", null);
                runner.shutdown();
                pageVariables.put("result", "system shutdown");
            } else {
                pageVariables.put("result", "system has not been launched or shutdown");
            }
        } else {
            pageVariables.put("result", "unknown action");
        }
        page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }

}
