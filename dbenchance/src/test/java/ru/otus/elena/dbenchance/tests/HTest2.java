
package ru.otus.elena.dbenchance.tests;

import java.util.HashSet;
import java.util.Set;
import ru.otus.elena.dbenchance.dataset.Compote;
import ru.otus.elena.dbenchance.dataset.Fruit;
import ru.otus.elena.dbenchance.dbservice.DBServiceHibernate;

public class HTest2 {
    public static void main(String[] args) {
        DBServiceHibernate service=new DBServiceHibernate();
        Fruit apple=new Fruit("apple",5);
        Fruit plum=new Fruit("plum",4);
        //service.save(apple);//не сохранится без ссылки на Compote
        //service.save(plum);//не сохранится без ссылки на Compote
        Set<Fruit>fruits=new HashSet<>();        
        fruits.add(apple);
        fruits.add(plum);
        System.out.println(fruits.toString());
        Compote compote=new Compote("mulifruit",fruits);
        service.save(compote);
        Fruit replum=(Fruit)service.loadById(2, Fruit.class);
        System.out.println(replum);
        Compote reComp=(Compote)service.loadById(1, Compote.class);
        Set<Fruit>refruits=reComp.getFruit();
        System.out.println(refruits.toString());
        System.out.println(reComp);
        service.shutDown(); 
    }
}
