
package ru.otus.elena.dbservice.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.elena.dbservice.configuration.ServiceConfiguration;
import ru.otus.elena.dbservice.dbservice.DBService;
import ru.otus.elena.dbservice.services.ServiceSetting;
import ru.otus.elena.dbservice.services.ServiceTest;
import ru.otus.elena.dbservice.services.TemplateProcessor;

public class DBServiceContext {
    private static ApplicationContext context;
    private static DBService service;
    private static ServiceSocket serviceSocket;
    private static TemplateProcessor templateProcessor;
    private static ServiceSetting serviceSetting;
    private static MessageHandlerService handler;
    private static ServiceTest serviceTest;
    private static MessageExecutor messageExecutor;
    
    public DBServiceContext(){
        context = new AnnotationConfigApplicationContext(ServiceConfiguration.class);
        service=context.getBean(DBService.class);
        serviceSocket=context.getBean(ServiceSocket.class);
        templateProcessor=context.getBean(TemplateProcessor.class);
        serviceSetting=context.getBean(ServiceSetting.class);
        handler=context.getBean(MessageHandlerService.class);
        serviceTest=context.getBean(ServiceTest.class);
        messageExecutor=context.getBean(MessageExecutor.class);
    }

    public static DBService getService() {
        return service;
    }

    public static ServiceSocket getServiceSocket() {
        return serviceSocket;
    }

    public static TemplateProcessor getTemplateProcessor() {
        return templateProcessor;
    }

    public static ServiceSetting getServiceSetting() {
        return serviceSetting;
    }

    public static MessageHandlerService getHandler() {
        return handler;
    }

    public static ServiceTest getServiceTest() {
        return serviceTest;
    }

    public static MessageExecutor getMessageExecutor() {
        return messageExecutor;
    }
    

}
