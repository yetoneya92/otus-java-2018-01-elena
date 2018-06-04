
package ru.otus.elena.dbenchance.tests;

import java.util.HashSet;
import java.util.Set;
import ru.otus.elena.cache.IdleTimeCache;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dbservice.ServiceSelf;

public class NativeCacheTest2 {
       
    public static void main(String[] args) {
       
        ServiceSelf service = ServiceSelf.getService("localhost", "db_example", "me", "me");
        IdleTimeCache cache=new IdleTimeCache(1000,1000);
        service.setCache(cache);
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
        service.save(compote);
        System.out.println("AFTER SAVE "+compote);    
        Fruit apple2 = new Fruit("apple", 1);
        Fruit plum2 = new Fruit("plum", 5);
        Set<Fruit> fruits2 = new HashSet<>();
        fruits2.add(apple2);
        fruits2.add(plum2);
        Compote compote2=new Compote("multifruit2",fruits2);
        System.out.println("BEFORE "+compote2);
        service.save(compote2);
        System.out.println("AFTER SAVE "+compote2);        
        Compote c1=service.loadById(1, Compote.class);
        System.out.println("AFTER LOAD "+c1);
        Compote c2 = service.loadById(2, Compote.class);
        System.out.println("AFTER LOAD " + c2);
        service.deleteAllTables();
        service.shutDown();
     }
}