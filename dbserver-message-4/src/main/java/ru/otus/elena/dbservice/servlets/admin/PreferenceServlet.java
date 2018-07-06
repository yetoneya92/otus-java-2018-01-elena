
package ru.otus.elena.dbservice.servlets.admin;

import ru.otus.elena.dbservice.services.TemplateProcessor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.cache.EternalCache;
import ru.otus.elena.dbservice.cache.IdleTimeCache;
import ru.otus.elena.dbservice.cache.LifeTimeCache;
import ru.otus.elena.dbservice.cache.SoftReferenceCache;
import ru.otus.elena.dbservice.dbservice.DBService;
import ru.otus.elena.dbservice.services.ServiceSetting;
import ru.otus.elena.dbservice.main.DBServiceContext;

public class PreferenceServlet extends HttpServlet {

    private static final String ACTION_PAGE_TEMPLATE = "adminactions.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private TemplateProcessor templateProcessor = DBServiceContext.getTemplateProcessor();
    private ServiceSetting serviceSetting = DBServiceContext.getServiceSetting();
    private DBService service = DBServiceContext.getService();

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page;
        try {
            String login = serviceSetting.getLogin();
            if (login == null) {
                pageVariables.put("result", "sign up");
                page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
            } else {
                if (service != null) {
                    String cache = request.getParameter("cache");
                    int maxElements;
                    switch (cache) {
                        case "idle":
                            long idleTimeMs = Long.parseLong(request.getParameter("time"));
                            maxElements = Integer.parseInt(request.getParameter("maxsize"));
                            IdleTimeCache idlecache = new IdleTimeCache(maxElements, idleTimeMs);
                            service.setCache(idlecache);
                            break;
                        case "life":
                            long lifeTimeMs = Long.parseLong(request.getParameter("time"));
                            maxElements = Integer.parseInt(request.getParameter("maxsize"));
                            LifeTimeCache lifecache = new LifeTimeCache(maxElements, lifeTimeMs);
                            service.setCache(lifecache);
                            break;
                        case "soft":
                            maxElements = Integer.parseInt(request.getParameter("maxsize"));
                            SoftReferenceCache softcache = new SoftReferenceCache(maxElements);
                            service.setCache(softcache);
                            break;
                        case "eternal":
                            maxElements = Integer.parseInt(request.getParameter("maxsize"));
                            EternalCache eternalcache = new EternalCache(maxElements);
                            service.setCache(eternalcache);
                            break;
                    }
                    pageVariables.put("result", "Service has set");
                }
            }
            page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            pageVariables.put("result", ex.getMessage());
            page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

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
            pageVariables.put("result", "");
            page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
