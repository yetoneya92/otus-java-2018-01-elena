
package ru.otus.elena.dbservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.elena.dbservice.services.TemplateProcessor;
import ru.otus.elena.dbservice.services.ServiceSetting;
import ru.otus.elena.cache.DBCache;
import ru.otus.elena.dbservice.dbservice.DBService;
import ru.otus.elena.dbservice.main.DBServiceContext;

public class CacheServlet extends HttpServlet {

    private static final String PARAMETERS_PAGE_TEMPLATE = "cacheparameters.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String ACTION_PAGE_TEMPLATE = "adminactions.html";
    private TemplateProcessor templateProcessor=DBServiceContext.getTemplateProcessor();;
    private ServiceSetting serviceSetting=DBServiceContext.getServiceSetting();
    private DBService service=DBServiceContext.getService();

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String login = serviceSetting.getLogin();
        if (login == null) {
            pageVariables.put("result", "");
            page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
        } else {
            if (service != null) {
                DBCache cache = service.getCache();
                if (cache != null) {
                    long size = cache.getCacheSize();
                    long hit = cache.getHitCount();
                    long miss = cache.getMissCount();
                    pageVariables.put("size", size);
                    pageVariables.put("hit", hit);
                    pageVariables.put("miss", miss);
                    page = templateProcessor.getPage(PARAMETERS_PAGE_TEMPLATE, pageVariables);
                } else {
                    pageVariables.put("result", "cache doesn't exists");
                    page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
                }
            } else {
                pageVariables.put("result", "service doesn't exists");
                page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
            }
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
}
