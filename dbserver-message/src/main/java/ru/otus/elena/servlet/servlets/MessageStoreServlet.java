
package ru.otus.elena.servlet.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.messages.message.MessageSystem;
import ru.otus.elena.messages.message.User;
import ru.otus.elena.servlet.services.TemplateProcessor;

public class MessageStoreServlet extends HttpServlet {
    
    private static final String USER_PAGE_TEMPLATE = "useraction.html";

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public MessageStoreServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public MessageStoreServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("clear")) {
            User user = (User) request.getSession().getAttribute("user");
            MessageSystem.getMessageSystem().getAnswerMap().get(user).clear();
            pageVariables.put("result", "messages has been cleared");
        } else {
            pageVariables.put("result", "");
        }
        page = templateProcessor.getPage(USER_PAGE_TEMPLATE, pageVariables);
        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
