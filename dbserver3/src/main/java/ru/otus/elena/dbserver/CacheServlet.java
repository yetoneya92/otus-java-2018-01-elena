
package ru.otus.elena.dbserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheServlet extends HttpServlet{
      private static final String SOMETHING_PAGE_TEMPLATE = "something.html";
      private static final String CACHE_PARAMETERS_PAGE_TEMPLATE = "cacheparameters.html";
      private static final String ACTIONS_PAGE_TEMPLATE="adminactions.html";

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public CacheServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public CacheServlet() throws IOException {
        this(new TemplateProcessor());
    }

      @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String page = null;
        Map<String, Object> pageVariables = new HashMap<>();
        String action = request.getParameter("param");

        if (action.equalsIgnoreCase("cache")) {
            page = templateProcessor.getPage(CACHE_PARAMETERS_PAGE_TEMPLATE, pageVariables);

        } else if (action.equalsIgnoreCase("something")) {
            page = templateProcessor.getPage(SOMETHING_PAGE_TEMPLATE, pageVariables);
        } else {
            page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
