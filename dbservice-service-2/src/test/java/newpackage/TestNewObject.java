
package newpackage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.elena.cache.IdleTimeCache;
import ru.otus.elena.dbservice.configuration.ServiceConfiguration;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dbservice.DBService;

public class TestNewObject {


    public static void main(String[] args) {
        new TestNewObject().startTest();
    }

    void startTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ServiceConfiguration.class);
        DBService service = context.getBean(DBService.class);
        service.setCache(new IdleTimeCache(1000, 1000));
        service.deleteAllTables();
        service.createTable(UnknownEmployee.class);
        service.createTable(UnknownFriend.class);
        service.createTable(UnknownPhone.class);
        Set<UnknownFriend>friends=new HashSet<>();
        friends.add(new UnknownFriend("Gosha",22));
        friends.add(new UnknownFriend("Pasha",33));
        System.out.println(service.save(new UnknownEmployee("Petya",new UnknownPhone(33333,2222),friends)));
        System.out.println(service.loadByName("Petya", UnknownEmployee.class).getObjects());
       // service.deleteAllTables();
        service.shutDown();
    }
}
