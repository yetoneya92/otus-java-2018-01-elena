
package ru.otus.elena.dbservice.servlets;

import ru.otus.elena.dbservice.services.TemplateProcessor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.cache.ServiceCache;
import ru.otus.elena.dbservice.main.DBServicePreference;
import ru.otus.elena.dbservice.main.MessageHandlerService;


public class ActionServlet extends HttpServlet{
      private static final String SOMETHING_PAGE_TEMPLATE = "something.html";
      private static final String PARAMETERS_PAGE_TEMPLATE = "cacheparameters.html";
      private static final String ACTIONS_PAGE_TEMPLATE="adminactions.html";
      private static final String LOGIN_PAGE_TEMPLATE = "login.html";
      private static final String PREFERENCES_PAGE_TEMPLATE = "preference.html";
      private static final String CREATETABLE_PAGE_TEMPLATE = "createtable.html";
      private static final String DELETETABLE_PAGE_TEMPLATE = "deletetable.html";
      private static final String TEST_PAGE_TEMPLATE = "test1.html";
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
        String page = null;
        Map<String, Object> pageVariables = new HashMap<>();
        try{
        String login = DBServicePreference.getDBServicePreference().getLogin();
        if (login == null) {
            pageVariables.put("result","");
            page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
        } else {
            Service service=DBServicePreference.getDBServicePreference().getService();
            String action = request.getParameter("action");
            switch (action) {
                case "setservice":
                    page = templateProcessor.getPage(PREFERENCES_PAGE_TEMPLATE, pageVariables);
                    break;
                case "create":
                    if (service != null) {
                        pageVariables.put("result","");
                        page = templateProcessor.getPage(CREATETABLE_PAGE_TEMPLATE, pageVariables);
                    } else {
                       pageVariables.put("result","has not done, service doesn't exist");
                        page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
                    }
                    break;
                case "delete":
                    if (service != null) {
                        pageVariables.put("result","");
                        page = templateProcessor.getPage(DELETETABLE_PAGE_TEMPLATE, pageVariables);
                    } else {
                        pageVariables.put("result","has not done, service doesn't exist");
                        page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
                    }
                    break;                  
                case "test":                    
                    pageVariables.put("result","");
                    page = templateProcessor.getPage(TEST_PAGE_TEMPLATE, pageVariables);
                    break;
                    case "cache":
                        if (service != null) {
                            ServiceCache cache = service.getCache();
                            if (cache != null) {
                                pageVariables.put("size", cache.getCacheSize());
                                pageVariables.put("hit", cache.getHitCount());
                                pageVariables.put("miss", cache.getMissCount());
                                page = templateProcessor.getPage(PARAMETERS_PAGE_TEMPLATE, pageVariables);
                            } else {
                                pageVariables.put("result", "cache not exists");
                                page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
                            }
                        }
                        break;
                    case "something":
                        page = templateProcessor.getPage(SOMETHING_PAGE_TEMPLATE, pageVariables);
                        break;
                    case "shutdown":
                        pageVariables.put("result", "service shutdown");
                        page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
                        MessageHandlerService.getMessageHandlerService().shutdownService();
                }
            }

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException ex) {
            pageVariables.put("result", ex.getMessage());
            page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
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
        String login = DBServicePreference.getDBServicePreference().getLogin();
        if (login == null) {
            pageVariables.put("result", "");
            page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
        } else {
            pageVariables.put("result", "");
            page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
