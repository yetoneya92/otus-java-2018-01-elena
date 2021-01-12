
package ru.otus.elena.dbservice.client.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.client.main.ClientMain;
import ru.otus.elena.dbservice.client.main.ClientSocket;
import ru.otus.elena.dbservice.client.main.MessageHandlerClient;
import ru.otus.elena.dbservice.message.Message;
import ru.otus.elena.dbservice.message.MessageContainer;

import ru.otus.elena.dbservice.client.services.TemplateProcessor;




public class ReadServlet extends HttpServlet{
            

    private static final String READ_PAGE_TEMPLATE = "read.html";
    private static final String ACTION_PAGE_TEMPLATE = "action.html";
    private static final String ERROR_PAGE_TEMPLATE = "error.html";
    private static final String SERVICE_PAGE_TEMPLATE = "service.html";
    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public ReadServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public ReadServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String page = null;
        Map<String, Object> pageVariables = new HashMap<>();
        MessageHandlerClient handler=ClientSocket.getClientSocket().getHandler();
        Object obj = null;
        try {
            if (handler == null) {
                pageVariables.put("result", "connection doesn't exists");
                page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
            } else {
                String serviceAddress = ClientSocket.getClientSocket().getServiceAddress();
                String tablename = request.getParameter("tablename");             
                String _id = request.getParameter("id");
                String name = request.getParameter("name");
                if (!_id.equalsIgnoreCase("")) {
                    long id = Long.parseLong(_id);
                    Message message = new Message(tablename, id);

                    handler.send(new MessageContainer(message, ClientMain.ADDRESS, serviceAddress));
                    pageVariables.put("result", "message has been send");
                    page = templateProcessor.getPage(READ_PAGE_TEMPLATE, pageVariables);
                } else if (!name.equalsIgnoreCase("")) {
                    Message message = new Message(tablename, name);
                    handler.send(new MessageContainer(message, ClientMain.ADDRESS, serviceAddress));
                    pageVariables.put("result", "message has been send");
                    page = templateProcessor.getPage(READ_PAGE_TEMPLATE, pageVariables);
                } else {
                    pageVariables.put("result", "message has not been  send");
                    page = templateProcessor.getPage(ERROR_PAGE_TEMPLATE, pageVariables);
                }
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException ex) {
            pageVariables.put("result", ex.getMessage());
            page = templateProcessor.getPage(ERROR_PAGE_TEMPLATE, pageVariables);
            System.out.println(ex.getMessage());
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("result", "");
        String page = templateProcessor.getPage(SERVICE_PAGE_TEMPLATE, pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

