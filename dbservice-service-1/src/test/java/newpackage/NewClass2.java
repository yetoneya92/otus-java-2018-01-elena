/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import ru.otus.elena.cache.IdleTimeCache;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Compote;
import ru.otus.elena.dbservice.dataset.Fruit;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dbservice.DBService;

public class NewClass2 {
        public static void main(String[] args) {
        System.out.println("tru");
        DBService service = DBService.getService();
        service.setCache(new IdleTimeCache(1000, 1000));
       
        service.createTable(Compote.class);
        service.createTable(Fruit.class);
        service.createTable(Phone.class);
        service.createTable(Baby.class);
        ArrayList<String> tableNames = service.getTableNames();
        System.out.println(tableNames);
        Baby babyKatya = new Baby("Katya", new Phone(12));
        //Baby babyOlya=new Baby("Olya",new Phone(23));
        service.save(babyKatya);
        Baby remake=service.loadById(1,Baby.class);
        System.out.println(remake);
        Phone p=service.loadById(1,Phone.class);
        System.out.println(p);        
        service.shutDown();
    }
}
