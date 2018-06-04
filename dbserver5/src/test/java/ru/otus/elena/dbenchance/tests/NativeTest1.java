

package ru.otus.elena.dbenchance.tests;

import java.util.ArrayList;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dbservice.ServiceSelf;

public class NativeTest1 {
    public static void main(String[] args) {
        ServiceSelf service=ServiceSelf.getService("localhost","db_example","me","me");
        service.deleteAllTables();
        service.createTable(Phone.class);
        service.createTable(Baby.class);
        ArrayList<String> tableNames = service.getTableNames();
        System.out.println(tableNames);
        Baby babyKatya=new Baby("Katya",new Phone(12));
        Baby babyOlya=new Baby("Olya",new Phone(23));
        service.saveAll(babyKatya,babyOlya);
        Baby remake=service.loadById(1,Baby.class);
        System.out.println(remake);
        Phone p=service.loadById(1,Phone.class);
        System.out.println(p);
        service.deleteTable(Baby.class);
        service.deleteTable(Phone.class);
        //service.deleteAllTables();
        service.shutDown();
    }
}
