
package ru.otus.elena.servlet.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.messages.message.User;
import ru.otus.elena.messages.message.MessageSystem;
import ru.otus.elena.messages.message.Message;
import ru.otus.elena.servlet.services.DBWriter;
import ru.otus.elena.servlet.services.TemplateProcessor;

public class UserDataServlet extends HttpServlet{  
    private static final String USER_PAGE_TEMPLATE = "useraction.html";    
    private static final String ERRORDATA_PAGE_TEMPLATE = "errordata.html";
    private final TemplateProcessor templateProcessor;
    private static final String BABY_PAGE_TEMPLATE = "baby.html";
    private static final String COMPOTE_PAGE_TEMPLATE = "compote.html";

    @SuppressWarnings("WeakerAccess")
    public UserDataServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public UserDataServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page=null;
        try {
            String name = request.getParameter("tablename");
            if (name.equalsIgnoreCase("baby")) {
                if (request.getParameter("phone").equalsIgnoreCase("")
                        || request.getParameter("babyname").equalsIgnoreCase("")) {
                    throw new IllegalArgumentException();
                }
                int phone = Integer.parseInt(request.getParameter("phone"));
                Baby baby = new Baby(request.getParameter("babyname"), new Phone(phone));

                boolean taken = MessageSystem.getMessageSystem().sendMessage(new Message((User) request.getSession().getAttribute("user"), baby));
                if (!taken) {
                    pageVariables.put("result", "system is not available");
                } else {
                    // boolean isWritten = (boolean) MessageSystem.getMassageSystem().getAnswer((User) request.getSession().getAttribute("user"));
                    // DBWriter writer = DBWriter.getWriter();
                    //boolean isWritten = writer.writeObject(baby);
                    // if (isWritten) {
                    //   pageVariables.put("result", "has done " + baby.toString());
                    // } else {
                    //   pageVariables.put("result", "has not done " + baby.toString());
                    //  }
                    pageVariables.put("result", "has accepted");
                }
                page = templateProcessor.getPage(BABY_PAGE_TEMPLATE, pageVariables);
            } else if (name.equalsIgnoreCase("compote")) {
                Set<Fruit> fruits = new HashSet<>();
                String fruiti;
                String numberi;
                s:
                for (int i = 1; i <= 10; i++) {
                    fruiti = "fruit" + i;
                    numberi = "number" + i;
                    String fruitname = request.getParameter(fruiti);
                    String num = request.getParameter(numberi);
                    if (fruitname.equalsIgnoreCase("") || num.equalsIgnoreCase("")){
                        break;
                    }
                    int number = Integer.parseInt(num);
                    if (number < 1 || number > Integer.MAX_VALUE) {
                        throw new IllegalArgumentException();
                    } else {
                        fruits.add(new Fruit(fruitname, number));

                    }
                }
                if (!request.getParameter("compotename").equalsIgnoreCase("") && !fruits.isEmpty()) {
                    Compote compote = new Compote(request.getParameter("compotename"), fruits);
                    boolean taken = MessageSystem.getMessageSystem().sendMessage(new Message((User) request.getSession().getAttribute("user"), compote));
                    if (!taken) {
                        pageVariables.put("result", "system is not available");
                    } else {
                        pageVariables.put("result", "has accepted");
                    }
                    page = templateProcessor.getPage(COMPOTE_PAGE_TEMPLATE, pageVariables);

                } else {
                    page = templateProcessor.getPage(ERRORDATA_PAGE_TEMPLATE, pageVariables);
                }
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {           
            page = templateProcessor.getPage(ERRORDATA_PAGE_TEMPLATE, pageVariables);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }
}
