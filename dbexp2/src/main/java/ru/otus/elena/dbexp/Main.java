
package ru.otus.elena.dbexp;

import java.sql.SQLException;
import java.util.ArrayList;
import ru.otus.elena.dataset.Owner;
import ru.otus.elena.dataset.Person;
import ru.otus.elena.dbservice.DBService;
/**
 * mysql> CREATE USER 'me'@'localhost' IDENTIFIED BY 'me';
 * mysql> GRANT ALL PRIVILEGES ON * . * TO 'me'@'localhost';
 * mysql> select user, host from mysql.user;
 * mysql> create database db_example;
 * mysql> SET GLOBAL time_zone = '+3:00';
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

       DBService service=new DBService("localhost","db_example","me","me");
        service.createTable(Owner.class);
        service.createTable(Person.class);
       ArrayList<String>tableNames=service.getTableNames();
       System.out.println(tableNames);
       Owner vasily=new Owner("Vasily",55);
       service.save(vasily);
       service.saveAll
        (new Person("sharic",2,new Owner("Feodor",44)),new Person("bobic",3,new Owner("Eduard",44)));
       Owner piotr=new Owner("Piotr",22);
       service.save(new Person("tuzic",1,piotr));
       service.save(new Person("richard",1,piotr));

        Person charles = new Person("sharles", 10, vasily);
        service.save(charles);
        System.out.println(vasily);
        System.out.println(charles);
        Person remake = service.load(1L, Person.class);
        System.out.println(remake);
        Owner reconstruct = service.load(3, Owner.class);
        System.out.println(reconstruct);
        remake = service.load(100, Person.class);
        
        if (remake == null) {
            System.out.println("impossible");
        }
        service.deleteTable(Person.class);
        service.deleteTable(Owner.class);
        //service.deleteAllTables();
        service.shutDown();
    }
}
