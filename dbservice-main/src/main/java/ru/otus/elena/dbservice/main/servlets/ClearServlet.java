
package ru.otus.elena.dbservice.main.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.main.main.SystemRunner;


public class ClearServlet extends HttpServlet {
    
    private static final String ACTION_PAGE_TEMPLATE = "action.html";

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public ClearServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public ClearServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("clear")) {
        SystemRunner runner=(SystemRunner) request.getSession().getAttribute("runner");
        runner.clearMessages();
            pageVariables.put("result", "messages has been cleared");
        } else {
            pageVariables.put("result", "");
        }
        page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
