
package ru.otus.elena.servlet.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.cache.DBCache;
import ru.otus.elena.servlet.services.TemplateProcessor;

public class CacheServlet extends HttpServlet {
    private static final String PARAMETERS_PAGE_TEMPLATE = "cacheparameters.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
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
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String login = (String) request.getSession().getAttribute("login");     
        if (login == null) {
            page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
        } else {
            DBCache cache=(DBCache) request.getSession().getAttribute("cache");

                long size = cache.getCacheSize();
                long hit = cache.getHitCount();
                long miss = cache.getMissCount();
                pageVariables.put("size", size);
                pageVariables.put("hit", hit);
                pageVariables.put("miss", miss);
                page = templateProcessor.getPage(PARAMETERS_PAGE_TEMPLATE, pageVariables);         
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
