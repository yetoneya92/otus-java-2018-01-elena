
package ru.otus.elena.dbservice.servlets.context;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.elena.dbservice.dbservice.DBServiceImpl;
import ru.otus.elena.dbservice.socket.MessageHandlerService;
import ru.otus.elena.dbservice.socket.ServiceSocket;

public class DBServiceContext {
     
    private  AnnotationConfigApplicationContext context;
    private static TemplateProcessor templateProcessor;
    private static ServiceSetting serviceSetting;
    private static DBServiceImpl dBService;
    private static MessageHandlerService handler;
    private static ServiceSocket serviceSocket;
    private static ServiceTest serviceTest;
    
    public DBServiceContext(AnnotationConfigApplicationContext context){
        this.context=context;
        this.templateProcessor=context.getBean(TemplateProcessor.class);
        this.serviceSetting=context.getBean(ServiceSetting.class);
        this.handler=context.getBean(MessageHandlerService.class);
        this.dBService=context.getBean(DBServiceImpl.class);
        this.serviceSocket=context.getBean(ServiceSocket.class);
        this.serviceTest=context.getBean(ServiceTest.class);
    }

    public static TemplateProcessor getTemplateProcessor() {
        return templateProcessor;
    }

    public static ServiceSetting getServiceSetting() {
        return serviceSetting;
    }

    public static DBServiceImpl getdBService() {
        return dBService;
    }

    public static MessageHandlerService getHandler() {
        return handler;
    }

    public static ServiceSocket getServiceSocket() {
        return serviceSocket;
    }

    public static ServiceTest getServiceTest() {
        return serviceTest;
    }
    
    
}
