
package ru.otus.elena.dbandcache;

import java.sql.SQLException;
/**
 * mysql> CREATE USER 'me'@'localhost' IDENTIFIED BY 'me';
 * mysql> GRANT ALL PRIVILEGES ON * . * TO 'me'@'localhost';
 * mysql> select user, host from mysql.user;
 * mysql> create database db_example;
 * mysql> SET GLOBAL time_zone = '+3:00';
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        System.out.println("Hello, world");

    }
}
