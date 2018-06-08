
package ru.otus.elena.servlet.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.messages.message.MessageSystem;
import ru.otus.elena.messages.message.User;
import ru.otus.elena.servlet.services.TemplateProcessor;

public class MessageServlet extends HttpServlet{
        

    private static final String MESSAGE_PAGE_TEMPLATE = "message.html";
    

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public MessageServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public MessageServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        User user=(User)request.getSession().getAttribute("user");
        ArrayList<String>list=MessageSystem.getMessageSystem().getAnswer(user);
        StringBuilder builder=new StringBuilder();
        for(String s:list){
            builder.append(s).append("<br/>");
        }
        System.out.println(builder.toString());        
        pageVariables.put("result",builder.toString());
        String page = templateProcessor.getPage(MESSAGE_PAGE_TEMPLATE, pageVariables);      
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }
    
}

