
package ru.otus.elena.dbenchance.tests;

import java.util.HashSet;
import java.util.Set;
import ru.otus.elena.dbandcache.dataset.Compote;
import ru.otus.elena.dbandcache.dataset.Fruit;
import ru.otus.elena.dbandcache.dbservice.DBServiceHibernate;
import ru.otus.elena.dbandcache.dbservice.DBServiceNative;

public class NativeHibernateTest1 {
    public static void main(String[] args) {
        DBServiceHibernate serviceH = new DBServiceHibernate();
        DBServiceNative serviceN = new DBServiceNative("localhost", "db_example", "me", "me");
        serviceN.deleteAllTables();
        serviceN.createTable(Compote.class);
        serviceN.createTable(Fruit.class);
        
        Fruit apple = new Fruit("apple", 13);
        Fruit plum = new Fruit("plum", 13);
        //service.save(apple);//не сохранится без ссылки на Compote
        //service.save(plum);//не сохранится без ссылки на Compote
        Set<Fruit> fruits = new HashSet<>();
        fruits.add(apple);
        fruits.add(plum);
        Compote compote1 = new Compote("mulifruit", fruits);
        System.out.println(compote1);
        
        serviceN.save(compote1);
        
        Compote cN1 = serviceN.loadById(1, Compote.class);
        System.out.println(cN1);
        
        Compote cH1 = serviceH.loadById(1, Compote.class);
        System.out.println(cH1);
        
        Fruit apple2 = new Fruit("apple", 11);
        Fruit plum2 = new Fruit("plum", 15);
        Set<Fruit> fruits2 = new HashSet<>();
        fruits2.add(apple2);
        fruits2.add(plum2);
        Compote compote2 = new Compote("multifruit2", fruits2);
        
        serviceH.save(compote2);
        
        Compote cH2 =serviceH.loadById(2,Compote.class);
        System.out.println(cH2);
        
        Compote cN2 =serviceN.loadById(2,Compote.class);//null не хочет
        
        serviceN.deleteAllTables();
        serviceN.shutDown();        
        serviceH.shutDown();
    }
}
