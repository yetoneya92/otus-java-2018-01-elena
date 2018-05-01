
package ru.otus.elena.dbenchance.tests;

import java.util.HashSet;
import java.util.Set;
import ru.otus.elena.cache.DBCacheImpl;
import ru.otus.elena.dbandcache.dataset.Compote;
import ru.otus.elena.dbandcache.dataset.Fruit;
import ru.otus.elena.dbandcache.dbservice.DBServiceNative;

public class NativeCacheTest2 {
       
    public static void main(String[] args) {
        //-Xms10m -Xmx10m
        DBServiceNative service = new DBServiceNative("localhost", "db_example", "me", "me");
        DBCacheImpl cache=new DBCacheImpl();
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
        Compote c2=service.loadById(2, Compote.class);
        System.out.println("AFTER LOAD "+c2);

        for(int i=0;i<100;i++){
            String name="com"+i;
            service.save(new Compote(name,fruits));
        }
                Compote compoteFromCache=(Compote)cache.get(2, Compote.class);
        System.out.println("FROM CACHE="+compoteFromCache);
        int[]block1=new int[1024*1024*164];
        int[]block2=new int[1024*1024*3];
        int[]block3=new int[1024*1024];
        int[]block=new int[1024*285];
       
        System.gc();
        compoteFromCache=(Compote)cache.get(2, Compote.class);//null 
        System.out.println("FROM CACHE="+compoteFromCache);
        service.deleteAllTables();
        service.shutDown(); 
     }
}