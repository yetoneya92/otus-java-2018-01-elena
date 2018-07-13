/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import ru.otus.elena.dbservice.DBServiceMain;
import ru.otus.elena.dbservice.cache.IdleTimeCache;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dataset.tables.Table;
import ru.otus.elena.dbservice.dbservice.DBServiceImpl;

@Service
public class TestCompote {
    @Autowired
    private DBServiceImpl service;

    public static void main(String[] args) {
        new TestCompote().startTest();
    }

    void startTest() {
       AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DBServiceMain.class);
        service=context.getBean(DBServiceImpl.class);
        service.setCache(new IdleTimeCache(1000, 1000));
        service.deleteAllTables();
        service.createTable(Compote.class);
        service.createTable(Fruit.class);
        Fruit apple = new Fruit("apple", 5);
        Fruit plum = new Fruit("plum", 5);
        //service.save(apple);//не сохранится без ссылки на Compote
        //service.save(plum);//не сохранится без ссылки на Compote
        Set<Fruit> fruits = new HashSet<>();
        fruits.add(apple);
        fruits.add(plum);
        Compote compote = new Compote("mulifruit", fruits);
        System.out.println("BEFORE "+compote);
        ArrayList<String>messages=service.save(compote);
        System.out.println("AFTER SAVE "+compote);
    
        Fruit apple2 = new Fruit("apple", 1);
        Fruit plum2 = new Fruit("plum", 5);
        Set<Fruit> fruits2 = new HashSet<>();
        fruits2.add(apple2);
        fruits2.add(plum2);
        Compote compote2=new Compote("multifruit2",fruits2);
        System.out.println("BEFORE "+compote2);
        messages.addAll(service.save(compote2));
        System.out.println("AFTER SAVE "+compote2);        
        Compote c1=(Compote) service.loadById(1, Compote.class).getObject();
        System.out.println("AFTER LOAD "+c1);
        Compote c2=(Compote) service.loadById(2, Compote.class).getObject();
        System.out.println("AFTER LOAD "+c2);
        System.out.println(messages);
        Fruit peach2=new Fruit("peach",3);
        fruits.add(peach2);
        Compote compote3=new Compote("withpeaches",fruits);
        service.save(compote3);
        System.out.println(service.loadByName("withpeaches", Compote.class));
        System.out.println(service.load(Compote.class));
        for (Table table : Table.values()) {
            System.out.println(table.name());
        }
        service.shutDown();
        System.out.println("shutdown");
    }
}
