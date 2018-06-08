
package ru.otus.elena.servlet.servlets;

import ru.otus.elena.servlet.services.TemplateProcessor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.cache.DBCache;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.messages.message.MessageSystem;

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
        String login = (String) request.getSession().getAttribute("login");

        if (login == null) {
            page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
        } else {
            String action = request.getParameter("action");
            switch (action) {
                case "setservice":
                    page = templateProcessor.getPage(PREFERENCES_PAGE_TEMPLATE, pageVariables);
                    break;
                case "create":
                    Service cservice = (Service) request.getSession().getAttribute("service");
                    if (cservice != null) {
                        pageVariables.put("result","");
                        page = templateProcessor.getPage(CREATETABLE_PAGE_TEMPLATE, pageVariables);
                    } else {
                       pageVariables.put("result","has not done, service doesn't exist");
                        page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
                    }
                    break;
                case "delete":
                    Service dservice = (Service) request.getSession().getAttribute("service");
                    if (dservice != null) {
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
                    DBCache cache = (DBCache) request.getSession().getAttribute("cache");
                    if (cache != null) {
                        pageVariables.put("size", cache.getCacheSize());
                        pageVariables.put("hit", cache.getHitCount());
                        pageVariables.put("miss", cache.getMissCount());
                        page = templateProcessor.getPage(PARAMETERS_PAGE_TEMPLATE, pageVariables);
                    } else {
                        pageVariables.put("result","cache not exists");
                        page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
                        }
                        break;
                    case "something":                      
                    page = templateProcessor.getPage(SOMETHING_PAGE_TEMPLATE, pageVariables);
                    break;
                case "shutdown":
                    Service service = (Service) request.getSession().getAttribute("service");
                        if (service != null) {
                            MessageSystem.getMessageSystem().dispose();
                            service.shutDown();
                            request.getSession().setAttribute("service", null);
                            pageVariables.put("result", "service shutdown");
                            page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
                        } else {
                            pageVariables.put("result", "service is not exists");
                            page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
                        }
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
}
