
package ru.otus.elena.dbandcache.dbservice;
import ru.otus.elena.dbandcache.interfaces.DBService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import ru.otus.elena.dbandcache.dao.DataSetDAO;
import ru.otus.elena.dbandcache.dataset.Baby;
import ru.otus.elena.dbandcache.dataset.Compote;
import ru.otus.elena.dbandcache.dataset.base.DataSet;
import ru.otus.elena.dbandcache.dataset.Fruit;
import ru.otus.elena.dbandcache.dataset.Phone;
import ru.otus.elena.cache.DBCache;


public class DBServiceHibernate implements DBService{
    

    private final SessionFactory sessionFactory;
    private boolean useCache;
    private DBCache cache;

    public DBServiceHibernate() {
        Configuration configuration = new Configuration();


        configuration.addAnnotatedClass(Phone.class);
        configuration.addAnnotatedClass(Baby.class);
        configuration.addAnnotatedClass(Compote.class);
        configuration.addAnnotatedClass(Fruit.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db_example");
        configuration.setProperty("hibernate.connection.username", "me");
        configuration.setProperty("hibernate.connection.password", "me");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        sessionFactory = createSessionFactory(configuration);
    }

    public DBServiceHibernate(Configuration configuration) {
        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
    public String getLocalStatus() {
        return runInSession(session -> {
            return session.getTransaction().getStatus().name();
        });
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }

    @Override
    public void createTable(Class<? extends DataSet> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<String> getTableNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteTable(Class<? extends DataSet> clazz) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAllTables() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends DataSet> int saveAll(T... data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends DataSet> void save(T object) {
        if (useCache) {
            cache.put(object);
        }
        try {
            try (Session session = sessionFactory.openSession()) {
                DataSetDAO dao = new DataSetDAO(session);
                dao.save(object);
            }
        } catch (Exception e) {
            System.out.println("has not saved: "+e.getMessage());
        }
    }
    
    @Override
    public <T extends DataSet> T loadById(long id, Class<T> clazz) {    
        if (useCache) {
            T data = cache.get(id,clazz);
            if (data != null) {
                return data;
            }
        }
        return runInSession(session -> {
            DataSetDAO dao = new DataSetDAO(session);
            return dao.loadById(id, clazz);
        });
    }

    @Override
    public <T extends DataSet> T loadByName(String name, Class<T> clazz) {
        return runInSession(session -> {
            DataSetDAO dao = new DataSetDAO(session);
            return dao.loadByName(name, clazz);
        });
    }

    @Override
    public <T extends DataSet> List<T> load(Class<T> clazz) {
               return runInSession(session -> {
            DataSetDAO dao = new DataSetDAO(session);
            return dao.readAll(clazz);
        });
    }
    @Override
    public void setCache(DBCache cache) {
        //this.useCache=true;
       // this.cache=cache;
    }
    @Override
    public void shutDown() {
       sessionFactory.close();
    }



}
