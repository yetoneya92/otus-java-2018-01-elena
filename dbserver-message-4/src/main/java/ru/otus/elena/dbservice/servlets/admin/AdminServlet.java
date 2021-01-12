
package ru.otus.elena.dbservice.servlets.admin;

import ru.otus.elena.dbservice.services.TemplateProcessor;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.main.DBServiceContext;
import ru.otus.elena.dbservice.services.ServiceSetting;

public class AdminServlet extends HttpServlet {

    private static final String ACTIONS_PAGE_TEMPLATE = "adminactions.html";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private TemplateProcessor templateProcessor=DBServiceContext.getTemplateProcessor();;
    private ServiceSetting serviceSetting=DBServiceContext.getServiceSetting();
 

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String login = serviceSetting.getLogin();       
        if (login == null) {
            pageVariables.put("result","");
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
