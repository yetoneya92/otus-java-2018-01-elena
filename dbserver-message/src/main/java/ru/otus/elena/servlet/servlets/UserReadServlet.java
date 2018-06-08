
package ru.otus.elena.servlet.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.servlet.services.DBReader;
import ru.otus.elena.servlet.services.TemplateProcessor;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.messages.message.User;
import ru.otus.elena.messages.message.MessageSystem;
import ru.otus.elena.messages.message.Message;

public class UserReadServlet extends HttpServlet{
            

    private static final String USERREAD_PAGE_TEMPLATE = "userread.html";
    private static final String USER_PAGE_TEMPLATE = "useraction.html";
    private static final String ERRORDATA_PAGE_TEMPLATE = "errordata.html";
    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public UserReadServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public UserReadServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String page = null;
        Map<String, Object> pageVariables = new HashMap<>();
        Service service = (Service) request.getSession().getAttribute("service");
        Object obj=null;
        try {
            if (service == null) {
                pageVariables.put("result", "service doesn't exists");
                page = templateProcessor.getPage(USER_PAGE_TEMPLATE, pageVariables);
            } else {
                String table = request.getParameter("tablename");             //
                String _id = request.getParameter("id");
                String name = request.getParameter("name");
                if (!_id.equalsIgnoreCase("")) {
                    long id = Long.parseLong(_id);
                    boolean taken = MessageSystem.getMessageSystem().sendMessage(new Message((User) request.getSession().getAttribute("user"), table, id));
                    if (!taken) {
                        pageVariables.put("result", "system is not available");
                    } else {
                       // obj = MessageSystem.getMassageSystem().getAnswer((User) request.getSession().getAttribute("user"));
                        // DBReader reader = DBReader.getReader();//
                        // Object obj = reader.readById(table, id);
                        //if (obj == null) {
                        //    pageVariables.put("result", "object has not been read");

                       // } else {
                       //     String str = obj.toString();
                       //     pageVariables.put("result", str);

                      //  }
                       pageVariables.put("result", "has accepted");
                    }
                    page = templateProcessor.getPage(USERREAD_PAGE_TEMPLATE, pageVariables);
                } else if (!name.equalsIgnoreCase("")) {
                    boolean taken = MessageSystem.getMessageSystem().sendMessage(new Message((User) request.getSession().getAttribute("user"), table, name));
                    if (!taken) {
                        pageVariables.put("result", "system is not available");
                    } else {
                        //obj = MessageSystem.getMassageSystem().getAnswer((User) request.getSession().getAttribute("user"));

                        // DBReader reader = DBReader.getReader();
                        //obj = reader.readByName(table, name);
                       // if (obj != null) {
                          //  String str = obj.toString();
                          //  pageVariables.put("result", str);
                      //  } else {
                      //      pageVariables.put("result", "object has not been loaded");
                     //   }
                     pageVariables.put("result", "has accepted");
                    }
                    page = templateProcessor.getPage(USERREAD_PAGE_TEMPLATE, pageVariables);
                } else {
                    page = templateProcessor.getPage(ERRORDATA_PAGE_TEMPLATE, pageVariables);
                }
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException ex) {
            page = templateProcessor.getPage(ERRORDATA_PAGE_TEMPLATE, pageVariables);
            System.out.println(ex.getMessage());
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
