
package ru.otus.elena.mavenproject2;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import ru.otus.elena.myarraylist.MyArrayList;

public class Main {

    public static void main(String[] args) {

        MyArrayList<String> strs = new MyArrayList<>();
        strs.add("abc");
        strs.add("abc");
        strs.add("cab");
        strs.add("cab");
        out.println(strs);
        strs.remove("abc");
        out.println(strs);
        strs.remove(2);
        out.println(strs);
        MyArrayList<Integer> nums = new MyArrayList<>();
        nums.add(9);
        nums.add(4);
        nums.add(2);
        nums.add(4);
        out.println(nums);
        Integer i = 9;
        nums.remove(i);
        out.println(nums);
        MyArrayList<String> strs2 = new MyArrayList<>(10);
        strs2.add("cba");
        strs2.add("cba");
        out.println(strs2);
        MyArrayList<String> strs3 = new MyArrayList<>(new ArrayList<String>() {
            {
                add("abc");
                add("def");
                add("ghi");
            }
        });
        out.println(strs3);
        out.println(strs3.size());
        strs3.add("klm");
        strs3.add("npo");
        out.println(strs3);
        System.out.println(strs3.size());
        System.out.println(strs3.indexOf("klm"));
        strs3.remove("def");
        out.println(strs3);
        out.println(strs3.get(0));
        out.println(strs3.size());
        out.println(strs3.remove(5));
        out.println(strs3);
        strs3.add(0, "xyz");
        out.println(strs3);
        strs3.add(3, "xyz");
        out.println(strs3);
        strs3.add(-3, "xyz");
        out.println(strs3);
        strs3.add(8, "xyz");
        out.println(strs3);
        out.println(strs3.set(8, "rst"));
        out.println(strs3);
        out.println(strs3.set(10, "rst"));
        out.println(strs3);
        out.println(strs);
        out.println(strs3.addAll(strs));
        out.println(strs3);
        out.println(strs3.addAll(-1, strs));
        strs.add("xyz");
        strs.add("yxz");
        out.println(strs);
        out.println(strs.size());
        out.println(strs3);
        strs3.removeAll(strs);
        out.println(strs3);
        strs3.removeAll(strs2);
        out.println(strs3);
        MyArrayList<String> strs4 = new MyArrayList<>(new ArrayList<String>() {
            {
                add("abc");
                add("def");
                add("ghi");
                add("cba");
                add("klm");
                add("xyz");
            }
        });
        out.println(strs4);
        out.println(strs3);
        strs3.retainAll(strs4);
        out.println(strs3);
        strs3.addAll(strs4);
        out.println(strs3);
        MyArrayList<String> strs5 = strs3.subList(2, 5);
        out.println(strs5);
        strs5 = strs3.subList(0, strs3.size());
        out.println(strs5);
        strs3.add(1, "abcd");
        strs3.add(5, "klmn");
        out.println(strs3);
        Iterator<String> it = strs3.iterator();
        while (it.hasNext()) {
            String s =  it.next();
            out.println(s);
            if (s.startsWith("a")) {
                it.remove();
            }
        }
        out.println(strs3);
        ListIterator<String> lit = strs3.listIterator();
        while (lit.hasNext()) {
            String s=lit.next();
            out.print(s+" ");
            if (s.startsWith("d")) {
                out.println();
                out.println("remove "+s);
                lit.remove();
            }
        }
        out.println();
        out.println();
        while (lit.hasPrevious()) {
            String s=lit.previous();
            out.print(s+" ");
            if (s.startsWith("x")) {
                out.println();
                out.println("remove "+s);
               lit.remove();
            }
        }
        out.println();
        out.println(strs3);
        lit = strs3.listIterator(10);
        while (lit.hasPrevious()) {
            lit.previous();
            if (lit.previousIndex() % 2 == 0) {
                lit.add("abc");
                
            }
        }
        out.println(strs3);
        strs3.set(0, "abcd");
        out.println(strs3);
        Collections.sort(strs3);
        out.println(strs3);
        strs3.forEach(e->out.println(e.toUpperCase()));
    }
}

