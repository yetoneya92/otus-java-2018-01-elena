

package ru.otus.elena.dbenchance.tests;

import java.util.ArrayList;
import ru.otus.elena.dbenchance.dataset.Baby;
import ru.otus.elena.dbenchance.dataset.Phone;
import ru.otus.elena.dbenchance.dbservice.DBServiceNative;

public class NTest1 {
    public static void main(String[] args) {
        DBServiceNative service=new DBServiceNative("localhost","db_example","me","me");
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
