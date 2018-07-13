
package ru.otus.elena.serializator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Person {

    String name;
    int age;
    boolean isBig;
    double height = 3.55;    
    ArrayList<String> friends;
    Set<String>food=new HashSet<>();
    Map<String, Integer> toys;
    Map<Integer, Double> weight = new HashMap<>();
    int[] legs = {1, 2, 3, 4};   
    Owner owner;
    transient LocalDate creationalDate;
    
    public Person(String name, int age,
            ArrayList<String> friends, Map<String, Integer> toys, boolean isBig, Owner owner) {
        this.name = name;
        this.age = age;
        this.friends = friends;
        this.toys = toys;
        this.isBig = isBig;
        this.owner = owner;
        weight.put(2015,12.0);
        weight.put(2016,15.15);
        food.add("meat");
        food.add("serial");              
        creationalDate=LocalDate.now();
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + ", isBig=" + isBig + ", legs=" + Arrays.toString(legs)
                + ", friends=" + friends + ", toys=" + toys + ", food=" + toys + ", weight" + weight
                + ", owner=" + owner.getName()
                + ", date=" + creationalDate + "]";

    }

    public void setCreationalDate() {
        creationalDate = LocalDate.now();
    }

}
