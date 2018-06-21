package ru.otus.elena.dbservice.client.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.client.main.ClientMain;
import ru.otus.elena.dbservice.client.main.ClientSocket;
import ru.otus.elena.dbservice.client.main.MessageHandlerClient;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.message.Message;
import ru.otus.elena.dbservice.message.MessageContainer;
import ru.otus.elena.dbservice.client.services.TemplateProcessor;

public class DataServlet extends HttpServlet {

    private static final String ACTION_PAGE_TEMPLATE = "action.html";
    private static final String ERROR_PAGE_TEMPLATE = "error.html";
    private static final String BABY_PAGE_TEMPLATE = "baby.html";
    private static final String COMPOTE_PAGE_TEMPLATE = "compote.html";
    private static final String SERVICE_PAGE_TEMPLATE = "service.html";
     private static final String CONNECTION_PAGE_TEMPLATE = "connection.html";
    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public DataServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public DataServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page = null;

        try {
            String serviceAddress = ClientSocket.getClientSocket().getServiceAddress();
            if (serviceAddress == null) {
                pageVariables.put("result", "set service");
                page = templateProcessor.getPage(SERVICE_PAGE_TEMPLATE, pageVariables);
            } else {
                String name = request.getParameter("tablename");
                if (name.equalsIgnoreCase("baby")) {
                    if (request.getParameter("phone").equalsIgnoreCase("")
                            || request.getParameter("babyname").equalsIgnoreCase("")) {
                        pageVariables.put("result", "invalid data");
                    } else {
                        int phone = Integer.parseInt(request.getParameter("phone"));
                        Baby baby = new Baby(request.getParameter("babyname"), new Phone(phone));
                        Message message = new Message(baby);
                        ClientSocket.getClientSocket().getHandler().send(new MessageContainer(message, ClientMain.ADDRESS, serviceAddress));
                        pageVariables.put("result", "has accepted");
                    }

                    page = templateProcessor.getPage(BABY_PAGE_TEMPLATE, pageVariables);
                } else if (name.equalsIgnoreCase("compote")) {
                    Set<Fruit> fruits = new HashSet<>();
                    String fruiti;
                    String numberi;

                    for (int i = 1; i <= 10; i++) {
                        fruiti = "fruit" + i;
                        numberi = "number" + i;
                        String fruitname = request.getParameter(fruiti);
                        String num = request.getParameter(numberi);
                        if (fruitname.equalsIgnoreCase("") || num.equalsIgnoreCase("")) {
                            break;
                        }
                        int number = Integer.parseInt(num);
                        fruits.add(new Fruit(fruitname, number));
                    }
                    if (!request.getParameter("compotename").equalsIgnoreCase("") && !fruits.isEmpty()) {
                        Compote compote = new Compote(request.getParameter("compotename"), fruits);
                        Message message = new Message(compote);
                        ClientSocket.getClientSocket().getHandler().send(new MessageContainer(message, ClientMain.ADDRESS, serviceAddress));
                        pageVariables.put("result", "has accepted");

                    } else {
                        pageVariables.put("result", "invalid data");

                    }
                    page = templateProcessor.getPage(COMPOTE_PAGE_TEMPLATE, pageVariables);
                }
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            pageVariables.put("result", e.getMessage());
            page = templateProcessor.getPage(ERROR_PAGE_TEMPLATE, pageVariables);
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
