
package ru.otus.elena.tester.main;

import java.util.ArrayList;
import ru.otus.elena.tester.example.ExampleTest;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> list = Tester.doWork(ExampleTest.class);
        System.out.println(list);
    }
}
