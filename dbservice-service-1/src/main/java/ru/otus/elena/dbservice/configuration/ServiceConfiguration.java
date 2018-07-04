
package ru.otus.elena.dbservice.configuration;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.elena.dbservice.dbservice.DBService;
import ru.otus.elena.dbservice.dbservice.LoadStandard;
import ru.otus.elena.dbservice.dbservice.SaveStandard;
import ru.otus.elena.dbservice.dbservice.ServiceConnection;
import ru.otus.elena.dbservice.dbservice.ServiceCreate;
import ru.otus.elena.dbservice.dbservice.ServiceLoad;
import ru.otus.elena.dbservice.dbservice.ServiceSave;
import ru.otus.elena.dbservice.dbservice.ServiceTable;
import ru.otus.elena.dbservice.execution.LoadBaby;
import ru.otus.elena.dbservice.execution.LoadCompote;
import ru.otus.elena.dbservice.execution.SaveBaby;
import ru.otus.elena.dbservice.execution.SaveCompote;
import ru.otus.elena.dbservice.execution.ServiceExecution;
import ru.otus.elena.dbservice.main.MessageExecutor;
import ru.otus.elena.dbservice.main.MessageHandlerService;
import ru.otus.elena.dbservice.services.ServiceSetting;
import ru.otus.elena.dbservice.main.ServiceSocket;
import ru.otus.elena.dbservice.services.ServiceTest;
import ru.otus.elena.dbservice.services.TemplateProcessor;

@Configuration
@ComponentScan
public class ServiceConfiguration {

    @Bean
    public MysqlDataSource source(){
            MysqlDataSource ds= new MysqlDataSource();
            ds.setDatabaseName("db_example");
            ds.setUser("me");
            ds.setServerName("localhost");
            ds.setPassword("me");
            return ds;
    }
    
    @Bean
    public DBService service(){
        return new DBService();
    }
    
    @Bean
    public ServiceCreate serviceCreate(){
        return new ServiceCreate();
    }
    
    @Bean 
    public  ServiceExecution serviceExecution(){
        return new ServiceExecution();
    }
    
    @Bean 
    public ServiceTable serviceTable(){
        return new ServiceTable();
    }
    @Bean
    public ServiceSave serviceSave(){
        return new ServiceSave();
    }
    
    @Bean
    public ServiceLoad serviceLoad(){
        return new ServiceLoad();
    }

    @Bean
    public SaveStandard saveStandard() {
        return new SaveStandard();
    }
    @Bean
    public LoadStandard loadStandard(){
        return new LoadStandard();
    }
    @Bean
    public SaveBaby saveBaby() {
        return new SaveBaby();
    }

    @Bean
    public LoadBaby loadBaby() {
        return new LoadBaby();
    }
    
    @Bean
    public SaveCompote saveCompote(){
        return new SaveCompote();
    }
    
    @Bean
    public LoadCompote loadCompote() {
        return new LoadCompote();
    }

    @Bean
    public ServiceConnection serviceConnection() {
        return new ServiceConnection();
    }

    @Bean
    public ServiceSetting servicePreference() {
        return new ServiceSetting();
    }

    @Bean
    public ServiceSocket serviceSocket() {
        return new ServiceSocket();
    }

    @Bean
    public MessageHandlerService handler() {
        return new MessageHandlerService();
    }

    @Bean
    public TemplateProcessor templateProcessor() throws IOException {
        return new TemplateProcessor();
    }
    
    @Bean
    public ServiceTest serviceTest(){
        return new ServiceTest();
    }
    
    @Bean
    public MessageExecutor messageExecutor(){
        return new MessageExecutor();
    }
}
