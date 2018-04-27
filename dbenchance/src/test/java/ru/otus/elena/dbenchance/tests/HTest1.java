
package ru.otus.elena.dbenchance.tests;

import ru.otus.elena.dbenchance.dataset.Baby;
import ru.otus.elena.dbenchance.dataset.Phone;
import ru.otus.elena.dbenchance.dbservice.DBServiceHibernate;

public class HTest1 {
    public static void main(String[] args) {
        DBServiceHibernate service=new DBServiceHibernate();
               
        Baby babyKatya=new Baby("Katya",new Phone(12));
        Baby babyOlya=new Baby("Olya",new Phone(23));
        service.save(babyKatya);
        service.save(babyOlya);
        Baby remake=(Baby)service.loadById(1,Baby.class);
        System.out.println(remake);
        Phone refresh=(Phone)service.loadById(1,Phone.class);
        System.out.println(refresh);

        service.shutDown();
    }
 
}
