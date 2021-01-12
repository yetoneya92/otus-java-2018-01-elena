package ru.otus.elena.dbservice.client.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.client.main.ClientSocket;
import ru.otus.elena.dbservice.client.services.TemplateProcessor;

public class ConnectionServlet extends HttpServlet {

    private static final String SERVICE_PAGE_TEMPLATE = "service.html";
    private static final String CONNECTION_PAGE_TEMPLATE = "connection.html";

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public ConnectionServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public ConnectionServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("open")) {
            ClientSocket.getClientSocket().start();
            pageVariables.put("result", "");
            page = templateProcessor.getPage(SERVICE_PAGE_TEMPLATE, pageVariables);
        } else if (action.equalsIgnoreCase("close")) {
            ClientSocket.getClientSocket().close();
            pageVariables.put("result", "");
            page = templateProcessor.getPage(CONNECTION_PAGE_TEMPLATE, pageVariables);
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("result", "");
        String page = templateProcessor.getPage(CONNECTION_PAGE_TEMPLATE, pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
