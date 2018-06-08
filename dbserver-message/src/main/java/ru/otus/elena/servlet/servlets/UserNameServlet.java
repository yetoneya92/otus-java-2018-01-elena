
package ru.otus.elena.servlet.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ru.otus.elena.messages.message.User;
import ru.otus.elena.messages.message.MessageSystem;
import ru.otus.elena.servlet.services.TemplateProcessor;

public class UserNameServlet extends HttpServlet{
        

    private static final String USER_ACTION_PAGE_TEMPLATE = "useraction.html";
    

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public UserNameServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public UserNameServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        
        String name=request.getParameter("username");
        HttpSession session=request.getSession();
        
        pageVariables.put("result","name: "+name);
        User user=new User(name,session);
        request.getSession().setAttribute("user", user);
        MessageSystem.getMessageSystem().addUser(user);
        
        //name
        String page = templateProcessor.getPage(USER_ACTION_PAGE_TEMPLATE, pageVariables);      
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
}
}