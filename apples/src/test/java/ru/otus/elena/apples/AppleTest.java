
package ru.otus.elena.apples;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class AppleTest {
    
    public AppleTest() {
    }

    @Test
    public void testCompareTo() {
        Apple goodApple = new Apple(10, "red");
        Apple badApple = new Apple(10, "blue");
        int com = badApple.compareTo(goodApple);
        if (com == -1) {
            System.out.println("яблоки разные");
        } else {
            System.out.println("плохой компаратор");
        }
        goodApple = new Apple(10, "red");
        Apple goodAppleToo = new Apple(10, "red");
        com = goodAppleToo.compareTo(goodApple);
        System.out.println(com);
        if (com == 0) {
            System.out.println("яблоки одинаковые");
        } else {
            System.out.println("плохой компаратор");
        }
        com = goodApple.compareTo(goodApple);
        System.out.println(com);
        if (com == 0) {
            System.out.println("одно и то же яблоко");
        } else {
            System.out.println("плохой компаратор");
        }
    }

}