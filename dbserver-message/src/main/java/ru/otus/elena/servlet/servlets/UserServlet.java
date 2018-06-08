
package ru.otus.elena.servlet.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.messages.message.User;
import ru.otus.elena.servlet.services.TemplateProcessor;

public class UserServlet extends HttpServlet{
    

    private static final String USER_NAME_PAGE_TEMPLATE = "username.html";
    private static final String USER_ACTION_PAGE_TEMPLATE = "useraction.html";

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
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        pageVariables.put("result", "");
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            page = templateProcessor.getPage(USER_ACTION_PAGE_TEMPLATE, pageVariables);
        } else {
            page = templateProcessor.getPage(USER_NAME_PAGE_TEMPLATE, pageVariables);
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
