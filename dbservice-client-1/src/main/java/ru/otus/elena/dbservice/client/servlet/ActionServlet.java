
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



public class ActionServlet extends HttpServlet {

    private static final String READ_PAGE_TEMPLATE = "read.html";
    private static final String WRITE_PAGE_TEMPLATE = "write.html";
    private static final String ACTION_PAGE_TEMPLATE = "action.html";
    private static final String SERVICE_PAGE_TEMPLATE = "service.html";

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public ActionServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public ActionServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;
        ClientSocket client=ClientSocket.getClientSocket();
        String serviceAddress = ClientSocket.getClientSocket().getServiceAddress();
        if (serviceAddress == null) {
            pageVariables.put("result", "service doesn't exists");
            page = templateProcessor.getPage(SERVICE_PAGE_TEMPLATE, pageVariables);
        } else {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase("write")) {
                pageVariables.put("result", "");
                page = templateProcessor.getPage(WRITE_PAGE_TEMPLATE, pageVariables);
            } else if (action.equalsIgnoreCase("read")) {
                pageVariables.put("result", "");
                page = templateProcessor.getPage(READ_PAGE_TEMPLATE, pageVariables);
            } else {
                pageVariables.put("result", "unknown action");
                page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);
            }
        }
        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("result", "");
        String page=null;
        if(ClientSocket.getClientSocket().getServiceAddress()==null){
         page = templateProcessor.getPage(SERVICE_PAGE_TEMPLATE, pageVariables);   
        }
        else{
        page = templateProcessor.getPage(ACTION_PAGE_TEMPLATE, pageVariables);}
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    
}


