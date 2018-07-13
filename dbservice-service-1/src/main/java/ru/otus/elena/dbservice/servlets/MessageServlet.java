
package ru.otus.elena.dbservice.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.servlets.context.DBServiceContext;
import ru.otus.elena.dbservice.socket.MessageHandlerService;
import ru.otus.elena.dbservice.servlets.context.TemplateProcessor;

public class MessageServlet extends HttpServlet{

    private static final String MESSAGE_PAGE_TEMPLATE = "message.html";
    private TemplateProcessor templateProcessor = DBServiceContext.getTemplateProcessor();
    private MessageHandlerService handler = DBServiceContext.getHandler();

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        ArrayList<String>list=handler.getInputMessages();
        if (list != null) {
            StringBuilder builder = new StringBuilder();
            for (String s : list) {
                builder.append(s).append("<br/>");
            }
            System.out.println(builder.toString());
            pageVariables.put("result", builder.toString());
        } else {
            pageVariables.put("result", "");
        }

        String page = templateProcessor.getPage(MESSAGE_PAGE_TEMPLATE, pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
    
}

