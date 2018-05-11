
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
    private static final String USERSTART_PAGE_TEMPLATE = "userstart.html";
    private static final String USERSTOP_PAGE_TEMPLATE = "userstop.html"; 
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
        if (action.equalsIgnoreCase("start")) {
            if (!isUsed) {
                isUsed = true;
                dbuser.useDB();
            }

            pageVariables.put("message", "Start of usage");
            page = templateProcessor.getPage(USERSTART_PAGE_TEMPLATE, pageVariables);
        } else if (action.equalsIgnoreCase("stop")) {
            if (isUsed) {
                dbuser.stopUse();
                isUsed = false;
            }
            pageVariables.put("message", "End of usage");
            page = templateProcessor.getPage(USERSTOP_PAGE_TEMPLATE, pageVariables);
        } else {
            pageVariables.put("message", "???");
        }
        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
