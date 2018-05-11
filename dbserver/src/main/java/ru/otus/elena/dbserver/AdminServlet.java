
package ru.otus.elena.dbserver;


import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class AdminServlet extends HttpServlet {
    

    private static final String ACTIONS_PAGE_TEMPLATE = "adminactions.html";
    private static final String ERROR_PAGE_TEMPLATE="errorpassword.html";

    private final TemplateProcessor templateProcessor;

    @SuppressWarnings("WeakerAccess")
    public AdminServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @SuppressWarnings("WeakerAccess")
    public AdminServlet() throws IOException {
        this(new TemplateProcessor());
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String page;
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("src/main/resources/password.json");
            JSONObject object = (JSONObject) parser.parse(reader);
            String login = (String) object.get("login");
            String password = (String) object.get("password");
            if (!(login.equalsIgnoreCase(request.getParameter("login"))
                    && password.equalsIgnoreCase(request.getParameter("password")))) {
                page = templateProcessor.getPage(ERROR_PAGE_TEMPLATE, pageVariables);             
            } else {
                page = templateProcessor.getPage(ACTIONS_PAGE_TEMPLATE, pageVariables);
                
            }

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(page);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
