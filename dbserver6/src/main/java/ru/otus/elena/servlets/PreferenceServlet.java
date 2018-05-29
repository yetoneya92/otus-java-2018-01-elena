
package ru.otus.elena.servlets;

import ru.otus.elena.servlet.services.TemplateProcessor;
import ru.otus.elena.servlet.services.ServiceTest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.elena.cache.EternalCache;
import ru.otus.elena.cache.IdleTimeCache;
import ru.otus.elena.cache.LifeTimeCache;
import ru.otus.elena.cache.SoftReferenceCache;
import ru.otus.elena.dbservice.dbservice.ServiceHibernate;
import ru.otus.elena.dbservice.dbservice.ServiceSelf;
import ru.otus.elena.dbservice.configuration.ServiceConfiguration;
import ru.otus.elena.servlet.services.DataReader;
import ru.otus.elena.servlet.services.DataWriter;
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
        String page=null;
        
        try {
            String login = (String) request.getSession().getAttribute("login");
            if (login == null) {
                page = templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
            } else {
                ApplicationContext context = (ApplicationContext) request.getSession().getAttribute("context");
                if (context == null) {
                    context = new AnnotationConfigApplicationContext(ServiceConfiguration.class);
                    request.getSession().setAttribute("context", context);
                }

                Service service = (Service) request.getSession().getAttribute("service");
                
                
                String serviceType = request.getParameter("servicetype");
                if (serviceType.equalsIgnoreCase("self")
                        &&(service==null||service.getClass().equals(ServiceHibernate.class))) {                   
                    service = context.getBean(ServiceSelf.class);
                } else if(serviceType.equalsIgnoreCase("hibernate")&&(service==null||service.getClass().equals(ServiceHibernate.class))) {
                    service = context.getBean(ServiceHibernate.class);
                } 
                String cache = request.getParameter("cache");
                int maxElement;
                switch (cache) {
                case "idle":
                    long idleTimeMs = Long.parseLong(request.getParameter("time"));
                    maxElement = Integer.parseInt(request.getParameter("maxsize"));
                    IdleTimeCache idleCache = context.getBean(IdleTimeCache.class);
                    idleCache.setMaxElement(maxElement);
                    idleCache.setIdleTimeMs(idleTimeMs);
                    // IdleTimeCache idlecache = context.getBean(IdleTimeCache.class,maxElements,idleTimeMs);
                    request.getSession().setAttribute("cache", idleCache);
                    service.setCache(idleCache);
                    break;
                case "life":
                    long lifeTimeMs = Long.parseLong(request.getParameter("time"));
                    maxElement = Integer.parseInt(request.getParameter("maxsize"));
                    LifeTimeCache lifeCache = context.getBean(LifeTimeCache.class);
                    lifeCache.setMaxElement(maxElement);
                    lifeCache.setIdleTimeMs(lifeTimeMs);
                    //LifeTimeCache lifecache = context.getBean(LifeTimeCache.class,maxElements,lifeTimeMs);
                    request.getSession().setAttribute("cache", lifeCache);                    
                    service.setCache(lifeCache);
                    break;
                case "soft":
                    maxElement = Integer.parseInt(request.getParameter("maxsize"));
                    SoftReferenceCache softCache = context.getBean(SoftReferenceCache.class);
                    softCache.setMaxElement(maxElement);
                    //SoftReferenceCache softcache = context.getBean(SoftReferenceCache.class,maxElements);
                    request.getSession().setAttribute("cache", softCache);
                    service.setCache(softCache);
                    break;
                case "eternal":
                    maxElement = Integer.parseInt(request.getParameter("maxsize"));
                    EternalCache eternalcache = context.getBean(EternalCache.class);
                    // EternalCache eternalcache = context.getBean(EternalCache.class, maxElements);
                    request.getSession().setAttribute("cache", eternalcache);
                    service.setCache(eternalcache);
                    break;
            }            
            request.getSession().setAttribute("service", service);//
            DataWriter writer = context.getBean(DataWriter.class);
            DataReader reader = context.getBean(DataReader.class);
            boolean setInWriter = writer.setService(service, login);
            boolean setInReader = reader.setService(service, login);
            request.getSession().setAttribute("writer", writer);
            request.getSession().setAttribute("reader", reader);
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

