
package ru.otus.elena.servlet.servlets;

import ru.otus.elena.servlet.services.TemplateProcessor;
import ru.otus.elena.servlet.services.DBTest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.elena.cache.EternalCache;
import ru.otus.elena.cache.IdleTimeCache;
import ru.otus.elena.cache.LifeTimeCache;
import ru.otus.elena.cache.SoftReferenceCache;
import ru.otus.elena.dbservice.dbservice.ServiceHibernate;
import ru.otus.elena.dbservice.dbservice.ServiceSelf;
import ru.otus.elena.servlet.services.DBReader;
import ru.otus.elena.servlet.services.DBWriter;
import ru.otus.elena.dbservice.interfaces.Service;



public class PreferenceServlet extends HttpServlet {

   
    
    private static final String ACTION_PAGE_TEMPLATE = "adminactions.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
   
    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public PreferenceServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
        
    }

    @SuppressWarnings("WeakerAccess")
    public PreferenceServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page;
        try{String login = (String) request.getSession().getAttribute("login");
        if (login == null) {
            page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
        } else {
        Service service = null;
        String serviceType = request.getParameter("servicetype");
            if (serviceType.equalsIgnoreCase("self")) {
                service = ServiceSelf.getService("localhost", "db_example", "me", "me");
            } else if (serviceType.equalsIgnoreCase("hibernate")) {
                service = (Service) new ServiceHibernate();
            } else {
                throw new IllegalArgumentException("unknown service");
            }
            String cache = request.getParameter("cache");
            int maxElements;
            switch (cache) {
                case "idle":
                long idleTimeMs = Long.parseLong(request.getParameter("time"));
                maxElements=Integer.parseInt(request.getParameter("maxsize"));
                IdleTimeCache idlecache = new IdleTimeCache(maxElements,idleTimeMs);
              
                request.getSession().setAttribute("cache", idlecache);
                service.setCache(idlecache);
                break;
            case "life":
                long lifeTimeMs=Long.parseLong(request.getParameter("time"));
                maxElements=Integer.parseInt(request.getParameter("maxsize"));
                LifeTimeCache lifecache=new LifeTimeCache(maxElements,lifeTimeMs);
                
                request.getSession().setAttribute("cache", lifecache); 
                service.setCache(lifecache);
                break;
            case "soft":
                maxElements=Integer.parseInt(request.getParameter("maxsize"));
                SoftReferenceCache softcache =new SoftReferenceCache(maxElements);
                
                request.getSession().setAttribute("cache", softcache);
                service.setCache(softcache);
                break;
            case "eternal":
                maxElements=Integer.parseInt(request.getParameter("maxsize"));
                EternalCache eternalcache =new EternalCache(maxElements);
               // EternalCache eternalcache = context.getBean(EternalCache.class, maxElements);
                request.getSession().setAttribute("cache", eternalcache);
                service.setCache(eternalcache);
                break;
        }   
            request.getSession().setAttribute("service", service);//
            
            DBWriter writer=DBWriter.getWriter();
            DBReader reader=DBReader.getReader();
            boolean setInWriter = writer.setService(service, login);
            boolean setInReader = reader.setService(service, login);
            if (setInReader && setInWriter) {
                    pageVariables.put("result", "Service has set");
                } else {
                    pageVariables.put("result", "Service has not set");
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
}

