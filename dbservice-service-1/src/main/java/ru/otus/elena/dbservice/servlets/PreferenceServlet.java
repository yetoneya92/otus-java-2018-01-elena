
package ru.otus.elena.dbservice.servlets;

import ru.otus.elena.dbservice.services.TemplateProcessor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.elena.cache.EternalCache;
import ru.otus.elena.cache.IdleTimeCache;
import ru.otus.elena.cache.LifeTimeCache;
import ru.otus.elena.cache.SoftReferenceCache;
import ru.otus.elena.dbservice.dbservice.DBService;
import ru.otus.elena.dbservice.services.ServiceSetting;
import ru.otus.elena.dbservice.main.MessageHandlerService;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.dbservice.main.DBServiceContext;
import ru.otus.elena.dbservice.main.ServiceSocket;

public class PreferenceServlet extends HttpServlet {

    private static final String ACTION_PAGE_TEMPLATE = "adminactions.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    //@Autowired
    private TemplateProcessor templateProcessor = DBServiceContext.getTemplateProcessor();
    //@Autowired
    private ServiceSetting serviceSetting = DBServiceContext.getServiceSetting();
    //@Autowired
    private DBService service = DBServiceContext.getService();
    //@Autowired
    private MessageHandlerService handler = DBServiceContext.getHandler();

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page;
        try {
            String login =serviceSetting.getLogin();
            if (login == null) {
                pageVariables.put("result", "sign up");
                page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
            } else {
                if(service!=null){
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
                    if (serviceSetting.isStarted()) {
                        handler.startExec();
                        pageVariables.put("result", "Service has set");

                    } else {
                        pageVariables.put("result", "Service has set, would you like set connection?");
                    }

                } else {
                    pageVariables.put("result", "Service is not exists");
                }
                page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
            }
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
