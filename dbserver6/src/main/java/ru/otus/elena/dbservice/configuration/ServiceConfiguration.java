
package ru.otus.elena.dbservice.configuration;

import com.mysql.cj.jdbc.MysqlDataSource;
import javax.activation.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.elena.cache.Cache;
import ru.otus.elena.cache.EternalCache;
import ru.otus.elena.cache.IdleTimeCache;
import ru.otus.elena.cache.LifeTimeCache;
import ru.otus.elena.cache.SoftReferenceCache;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dbservice.ServiceHibernate;
import ru.otus.elena.dbservice.dbservice.ServiceSelf;
import ru.otus.elena.dbservice.interfaces.Service;
import ru.otus.elena.servlet.services.DataReader;
import ru.otus.elena.servlet.services.DataWriter;
import ru.otus.elena.servlet.services.ServiceTest;


@Configuration
@ComponentScan

public class ServiceConfiguration {

    @Bean
    public Cache eternalCache() {
        return new EternalCache();
    }

    @Bean
    public Cache idleTimeCache() {
        return new IdleTimeCache();
    }

    @Bean
     public Cache lifeTimeCache() {
        return new LifeTimeCache();
    }

    @Bean
    public Cache softReferenceCache() {
        return new SoftReferenceCache();
    }
    @Bean
    public Service serviceSelf() {
        return new ServiceSelf();
    }
    
   // @Bean
   // public Service serviceHibernate() {
   //     return new ServiceHibernate();
  //  }
    @Bean
    public ServiceTest serviceTest() {
        return new ServiceTest();
    }

    @Bean
    public DataWriter dataWriter() {
        return new DataWriter();
    }

    @Bean
    public DataReader dataReader() {
        return new DataReader();
    }

    @Bean
    public Compote compote() {
        return new Compote();
    }

    @Bean
    public Fruit fruit() {
        return new Fruit();
    }

    @Bean
    public Baby baby() {
        return new Baby();
    }

    @Bean
    public Phone phone() {
        return new Phone();
    }
    @Bean
    public MysqlDataSource dataSource(){
            MysqlDataSource ds= new MysqlDataSource();
            ds.setDatabaseName("db_example");
            ds.setUser("me");
            ds.setServerName("localhost");
            ds.setPassword("me");
            return ds;
    }
}
