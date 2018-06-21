
package ru.otus.elena.dbservice.client.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.client.main.ClientSocket;
import ru.otus.elena.dbservice.client.main.MessageHandlerClient;
import ru.otus.elena.dbservice.client.services.TemplateProcessor;

public class ServiceServlet extends HttpServlet {
    
    private static final String ACTION_PAGE_TEMPLATE = "action.html";
    private static final String CONNECTION_PAGE_TEMPLATE = "connection.html";
    private static final String SERVICE_PAGE_TEMPLATE = "service.html";

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public ServiceServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public ServiceServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String service = request.getParameter("service");
        ClientSocket.getClientSocket().setServiceAddress(service);
        pageVariables.put("result", "");
        String page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

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

