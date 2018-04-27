
package ru.otus.elena.dbenchance.tests;

import java.util.HashSet;
import java.util.Set;
import ru.otus.elena.dbenchance.dataset.Compote;
import ru.otus.elena.dbenchance.dataset.Fruit;
import ru.otus.elena.dbenchance.dbservice.DBServiceHibernate;
import ru.otus.elena.dbenchance.dbservice.DBServiceNative;

public class NTest2 {

    public static void main(String[] args) {
        DBServiceNative service = new DBServiceNative("localhost", "db_example", "me", "me");
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
        //System.out.println(fruits.toString());
        Compote compote = new Compote("mulifruit", fruits);
        //System.out.println(compote);
        service.save(compote);
        Compote c=service.loadById(1,Compote.class);       
        //System.out.println(c);
        Fruit apple2 = new Fruit("apple", 1);
        Fruit plum2 = new Fruit("plum", 5);
        Set<Fruit> fruits2 = new HashSet<>();
        fruits2.add(apple2);
        fruits2.add(plum2);
        Compote compote2=new Compote("multifruit2",fruits2);
        System.out.println("BEFORE 2"+compote2);
        service.save(compote2);
        Compote c1=service.loadById(1, Compote.class);
        System.out.println("AFTER 1"+c1);
        Compote c2=service.loadById(2, Compote.class);
        System.out.println("AFTER 2"+c2);
        service.deleteAllTables();
        service.shutDown();
        

    }
}
