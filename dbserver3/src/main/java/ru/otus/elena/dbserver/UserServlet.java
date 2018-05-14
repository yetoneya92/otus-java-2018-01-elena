
package ru.otus.elena.dbserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {

    private DBUser dbuser = new DBUser();
    private boolean isUsed;
    private static final String WORK_PAGE_TEMPLATE = "userstart.html";
    private static final String STOP_PAGE_TEMPLATE = "userstop.html"; 
    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public UserServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public UserServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String action = request.getParameter("writer");
        String page=null;
        String use=(String)request.getSession().getAttribute("use"); 
        if (action.equalsIgnoreCase("start")) {         
            if (use==null) {          
                dbuser.useDB();
                request.getSession().setAttribute("use","use");
            }
            pageVariables.put("message", "Use");
            page = templateProcessor.getPage(WORK_PAGE_TEMPLATE, pageVariables);
        } else if (action.equalsIgnoreCase("stop")) {
            
            if (use!=null) {
                dbuser.stopUse();
                request.getSession().setAttribute("use",null);
            }
            pageVariables.put("message", "Stop");
            page = templateProcessor.getPage(STOP_PAGE_TEMPLATE, pageVariables);
        } else {
            pageVariables.put("message", "Hello");
        }
        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String use = (String) request.getSession().getAttribute("use");
        if (use != null) {
            pageVariables.put("message", "Use");
            page = templateProcessor.getPage(WORK_PAGE_TEMPLATE, pageVariables);
        } else {
            pageVariables.put("message", "Stop");
            page = templateProcessor.getPage(STOP_PAGE_TEMPLATE, pageVariables);
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}

