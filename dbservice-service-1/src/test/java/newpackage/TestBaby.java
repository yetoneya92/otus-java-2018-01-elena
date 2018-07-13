/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.elena.dbservice.DBServiceMain;
import ru.otus.elena.dbservice.cache.IdleTimeCache;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dataset.base.DataSet;
import ru.otus.elena.dbservice.dbservice.DBServiceImpl;


public class TestBaby {

    public static void main(String[] args) {
        new TestBaby().startTest();
    }

    void startTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DBServiceMain.class);
        DBServiceImpl service=context.getBean(DBServiceImpl.class);
        service.setCache(new IdleTimeCache(1000, 1000));
        service.deleteAllTables();
        service.createTable(Phone.class);
        service.createTable(Baby.class);
        ArrayList<String> tableNames = service.getTableNames();
        System.out.println(tableNames);
        Baby babyKatya = new Baby("Katya", new Phone(12,123));
        Baby babyOlya = new Baby("Olya", new Phone(23,234));
        Baby babyKatya2 = new Baby("Katya", new Phone(99,999));
        service.save(babyKatya);
        service.save(babyKatya2);
        Baby remake = (Baby)service.loadById(1, Baby.class).getObject();
        System.out.println(remake);
        Phone p = (Phone) service.loadById(1, Phone.class).getObject();
        Phone p2 = new Phone(122,1222);
        System.out.println(p);
        ArrayList<DataSet> list = new ArrayList<>();
        list.add(p2);
        list.add(babyKatya);
        list.add(babyOlya);
        ArrayList<String> messages = service.saveAll(list);        
        System.out.println(messages);
        System.out.println("object: "+service.loadByName("Katya", Baby.class).getObjects());
        System.out.println("messages: "+service.loadByName("Katya", Baby.class).getMessages());
        ArrayList<Baby>after=(ArrayList<Baby>)service.load( Baby.class).getObjects();
        System.out.println("unknown: "+service.loadByName("Katy", Baby.class));
        System.out.println(after);
        service.shutDown();
        context.close();
    }
}
