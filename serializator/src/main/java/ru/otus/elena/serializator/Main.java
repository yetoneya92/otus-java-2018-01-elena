
package ru.otus.elena.serializator;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ArrayList<String>friends=new ArrayList<>();
        Map<String,Integer>toys=new HashMap<>();
        Person personSharic=new Person("Sharic",5,friends,toys,true,new Owner("Piotr",33));
        personSharic.friends.add("Bobic");
        personSharic.friends.add("Tuzic");
        personSharic.toys.put("ball", 2);
        personSharic.toys.put("bone", 3);       
        System.out.println(personSharic);
        
        Gson gson=new Gson();
        Serializator serializator=new Serializator();       
        String sSharic=serializator.toGson(personSharic);
        System.out.println(sSharic);
        Person reSharic=gson.fromJson(sSharic,Person.class);
        System.out.println(reSharic);
    }
}